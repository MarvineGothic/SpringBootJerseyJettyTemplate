package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.database.entity.Address;
import org.example.database.entity.User;
import org.example.database.repository.UserRepository;
import org.example.error.ServiceException;
import org.example.model.request.UserRequestDto;
import org.example.model.response.UserResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressService addressService;
    private final NotificationService notificationService;

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
        notificationService.sendMessage("UserCreated", user);
        var address = Address.builder()
                .address("Copenhagen")
                .user(user)
                .build();
        addressService.addAddress(address);
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