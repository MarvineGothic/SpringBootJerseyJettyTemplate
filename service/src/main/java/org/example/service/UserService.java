package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.database.entity.User;
import org.example.database.repository.UserRepository;
import org.example.error.ServiceException;
import org.example.event.EventType;
import org.example.event.SqsMessagingService;
import org.example.event.UserCreatedEvent;
import org.example.model.request.UserRequestDto;
import org.example.model.response.UserResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final SqsMessagingService notificationService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public List<UserResponseDto> getUsers() {
        var users = userRepository.findAll();
        return users.stream().map(UserService::mapUserResponse).toList();
    }

    public UserResponseDto getUser(Long id) {
        var user = userRepository.findById(id).orElseThrow(() -> new ServiceException("User not found", 404));
        return UserService.mapUserResponse(user);
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) throws ServiceException {
        if (userRepository.findByEmail(userRequestDto.getEmail()) != null) {
            throw new ServiceException("User already exist", 400);
        }
        var user = User.builder()
                .firstName(userRequestDto.getFirstName())
                .lastName(userRequestDto.getLastName())
                .email(userRequestDto.getEmail())
                .password(userRequestDto.getPassword())
                .accessRole(userRequestDto.getAccessRole())
                .build();

        user = userRepository.save(user);
        notificationService.sendMessage(EventType.USER_CREATED, user);
        applicationEventPublisher.publishEvent(new UserCreatedEvent(this, user));

        LOGGER.info("\nUser created");
        return mapUserResponse(user);
    }

    public static UserResponseDto mapUserResponse(User user) {
        return UserResponseDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .accessRole(user.getAccessRole())
                .build();
    }
}