package org.example.application.service.user;

import lombok.RequiredArgsConstructor;
import org.example.application.service.address.UserAddressService;
import org.example.domain.entity.User;
import org.example.domain.event.EventPublisher;
import org.example.domain.event.MessageSender;
import org.example.domain.event.UserCreatedEvent;
import org.example.domain.repository.UserRepository;
import org.example.error.ServiceException;
import org.example.event.EventType;
import org.example.model.request.UserRequestModel;
import org.example.model.response.AddressResponseModel;
import org.example.model.response.UserResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService { // Use Case Interactor
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserAddressService userAddressService;
    private final MessageSender notificationService;
    private final EventPublisher eventPublisher;

    @Override
    @Transactional
    public List<UserResponseModel> getUsers() {
        var users = userRepository.getUsers();
        return users.stream().map(UserServiceImpl::mapUserResponse).toList();
    }

    @Override
    @Transactional
    @Cacheable("users")
    public UserResponseModel getUser(long id) {
        var user = userRepository.getUserById(id).orElseThrow(() -> new ServiceException("User not found", 404));
        return mapUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponseModel createUser(UserRequestModel userRequestModel) throws ServiceException {
        userRepository.getUserByEmail(userRequestModel.getEmail())
                .ifPresent(user -> { throw new ServiceException("User already exist", HttpStatus.BAD_REQUEST.value()); });

        var user = User.builder()
                .firstName(userRequestModel.getFirstName())
                .lastName(userRequestModel.getLastName())
                .email(userRequestModel.getEmail())
                .password(userRequestModel.getPassword())
                .accessRole(userRequestModel.getAccessRole())
                .creationTime(LocalDateTime.now())
                .build();
        user = userRepository.createUser(user);
        for (var addressRequest : userRequestModel.getAddresses()) {
            userAddressService.addAddress(user.getId(), addressRequest);
        }

        notificationService.sendMessage(EventType.USER_CREATED, user);
        eventPublisher.publish(new UserCreatedEvent(this, user));

        LOGGER.info("\nUser created");
        return mapUserResponse(user);
    }

    @Override
    @Transactional
    public Set<AddressResponseModel> getUserAddresses(long id) {
        var user = userRepository.getUserByIdWithAddresses(id).orElseThrow(() -> new ServiceException("User not found", 404));
//        return user.getAddresses().stream().map(UserServiceImpl::mapAddressResponse).collect(Collectors.toSet());
        return null;
    }

    // Presenter logic
    public static UserResponseModel mapUserResponse(User user) {
//        var addressesResponse = user.getAddresses().stream().map(UserServiceImpl::mapAddressResponse).collect(Collectors.toSet());
        return UserResponseModel.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .accessRole(user.getAccessRole())
                .createdDate(user.getCreationTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
//                .addresses(addressesResponse.isEmpty() ? null : addressesResponse)
                .build();
    }
}