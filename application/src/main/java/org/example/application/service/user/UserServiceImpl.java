package org.example.application.service.user;

import com.github.f4b6a3.ulid.Ulid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.service.address.UserAddressService;
import org.example.authentication.BasicAuthenticationService;
import org.example.authentication.JwtAuthenticationService;
import org.example.domain.entity.User;
import org.example.domain.event.EventPublisher;
import org.example.domain.event.MessageSender;
import org.example.domain.event.UserCreatedEvent;
import org.example.domain.repository.UserRepository;
import org.example.error.ServiceException;
import org.example.event.EventType;
import org.example.model.request.CreateUserRequestModel;
import org.example.model.request.UserLoginRequestModel;
import org.example.model.response.AddressResponseModel;
import org.example.model.response.UserResponseModel;
import org.example.model.response.UserSessionResponseModel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService { // Use Case Interactor

    private final BasicAuthenticationService basicAuthenticationService;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final UserRepository userRepository;
    private final UserAddressService userAddressService;
    private final MessageSender notificationService;
    private final EventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserSessionResponseModel login(UserLoginRequestModel userLoginRequestModel) {
        var user = userRepository.getUserByEmail(userLoginRequestModel.getEmail())
                .orElseThrow(() -> new ServiceException("Bad credentials", HttpStatus.BAD_REQUEST.value()));

        if (!passwordEncoder.matches(userLoginRequestModel.getPassword(), user.getPassword())) {
            throw new ServiceException("Bad credentials", HttpStatus.BAD_REQUEST.value());
        }

        return UserSessionResponseModel.builder()
                .sessionToken(jwtAuthenticationService.generateToken(user.getHandle(), user.getEmail(), user.getAccessRole().name()))
                .build();
    }

    @Deprecated
    @Override
    public UserSessionResponseModel loginBasic(UserLoginRequestModel userLoginRequestModel) {
        var user = userRepository.getUserByEmail(userLoginRequestModel.getEmail())
                .orElseThrow(() -> new ServiceException("Bad credentials", HttpStatus.BAD_REQUEST.value()));

        if (!passwordEncoder.matches(userLoginRequestModel.getPassword(), user.getPassword())) {
            throw new ServiceException("Bad credentials", HttpStatus.BAD_REQUEST.value());
        }

        return UserSessionResponseModel.builder()
                .sessionToken(basicAuthenticationService.generateToken(user.getEmail(), user.getPassword()))
                .build();
    }

    @Override
    @Transactional
    public List<UserResponseModel> getUsers() {
        var users = userRepository.getUsers();
        return users.stream().map(UserServiceImpl::mapUserResponse).toList();
    }

    @Override
    @Transactional
    @Cacheable("users")
    public UserResponseModel getUser(String handle) {
        var user = userRepository.getUserByHandle(handle).orElseThrow(() -> new ServiceException("User not found", 404));
        return mapUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponseModel createUser(CreateUserRequestModel createUserRequestModel) throws ServiceException {
        userRepository.getUserByEmail(createUserRequestModel.getEmail())
                .ifPresent(user -> {
                    throw new ServiceException("User already exist", HttpStatus.BAD_REQUEST.value());
                });

        var user = User.builder()
                .handle(Ulid.fast().toLowerCase())
                .firstName(createUserRequestModel.getFirstName())
                .lastName(createUserRequestModel.getLastName())
                .email(createUserRequestModel.getEmail())
                .password(passwordEncoder.encode(createUserRequestModel.getPassword()))
                .accessRole(createUserRequestModel.getAccessRole())
                .creationTime(LocalDateTime.now())
                .addresses(List.of())
                .build();
        user = userRepository.createUser(user);
        if (createUserRequestModel.getAddresses() != null) {
            for (var addressRequest : createUserRequestModel.getAddresses()) {
                userAddressService.addAddress(user.getHandle(), addressRequest);
            }
        }

        notificationService.sendMessage(EventType.USER_CREATED, user);
        eventPublisher.publish(new UserCreatedEvent(this, user));

        log.info("\nUser created {}", user.toString());
        return mapUserResponse(user);
    }

    @Override
    @Transactional
    public Set<AddressResponseModel> getUserAddresses(String handle) {
        var user = userRepository.getUserByHandleWithAddresses(handle).orElseThrow(() -> new ServiceException("User not found", 404));
//        return user.getAddresses().stream().map(UserServiceImpl::mapAddressResponse).collect(Collectors.toSet());
        return null;
    }

    // Presenter logic
    public static UserResponseModel mapUserResponse(User user) {
//        var addressesResponse = user.getAddresses().stream().map(UserServiceImpl::mapAddressResponse).collect(Collectors.toSet());
        return UserResponseModel.builder()
                .handle(user.getHandle())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .accessRole(user.getAccessRole())
                .createdDate(user.getCreationTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
//                .addresses(addressesResponse.isEmpty() ? null : addressesResponse)
                .build();
    }
}