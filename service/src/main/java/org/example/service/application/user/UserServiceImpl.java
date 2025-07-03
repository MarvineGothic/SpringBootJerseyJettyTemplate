package org.example.service.application.user;

import lombok.RequiredArgsConstructor;
import org.example.error.ServiceException;
import org.example.event.EventType;
import org.example.event.SqsMessagingService;
import org.example.model.request.UserRequestModel;
import org.example.model.response.AddressResponseModel;
import org.example.model.response.UserResponseModel;
import org.example.service.domain.entity.UserEntity;
import org.example.service.domain.event.UserCreatedEvent;
import org.example.service.domain.repository.UserRepository;
import org.example.service.infrastructure.persistence.entity.Address;
import org.example.service.infrastructure.persistence.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final SqsMessagingService notificationService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public List<UserResponseModel> getUsers() {
        var users = userRepository.findAll();
        return users.stream().map(UserServiceImpl::mapUserResponse).toList();
    }

    @Override
    @Transactional
    @Cacheable("users")
    public UserResponseModel getUser(Long id) {
        var user = userRepository.findById(id).orElseThrow(() -> new ServiceException("User not found", 404));
        return mapUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponseModel createUser(UserRequestModel userRequestModel) throws ServiceException {
        userRepository.findByEmail(userRequestModel.getEmail())
                .ifPresent(user -> { throw new ServiceException("User already exist", HttpStatus.BAD_REQUEST.value()); });

        var user = (UserEntity) User.builder()
                .firstName(userRequestModel.getFirstName())
                .lastName(userRequestModel.getLastName())
                .email(userRequestModel.getEmail())
                .password(userRequestModel.getPassword())
                .accessRole(userRequestModel.getAccessRole())
                .creationTime(LocalDateTime.now())
                .addresses(Set.of(Address.builder()
                                .street("Valdemarsgade")
                                .house("34")
                                .country("Denmark")
                        .build()))
                .build();
        user = userRepository.save(user);

        notificationService.sendMessage(EventType.USER_CREATED, user);
        applicationEventPublisher.publishEvent(new UserCreatedEvent(this, user));

        LOGGER.info("\nUser created");
        return mapUserResponse(user);
    }

    @Override
    @Transactional
    public Set<AddressResponseModel> getUserAddresses(Long id) {
        var user = userRepository.findByIdWithAddresses(id).orElseThrow(() -> new ServiceException("User not found", 404));
        return user.getAddresses().stream().map(UserServiceImpl::mapAddressResponse).collect(Collectors.toSet());
    }

    public static UserResponseModel mapUserResponse(UserEntity user) {
        var addressesResponse = user.getAddresses().stream().map(UserServiceImpl::mapAddressResponse).collect(Collectors.toSet());
        return UserResponseModel.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .accessRole(user.getAccessRole())
                .createdDate(user.getCreationTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .addresses(addressesResponse.isEmpty() ? null : addressesResponse)
                .build();
    }

    public static AddressResponseModel mapAddressResponse(Address address) {
        return AddressResponseModel.builder()
                .house(address.getHouse())
                .floor(address.getFloor())
                .street(address.getStreet())
                .postCode(address.getPostCode())
                .city(address.getCity())
                .country(address.getCountry())
                .build();
    }
}