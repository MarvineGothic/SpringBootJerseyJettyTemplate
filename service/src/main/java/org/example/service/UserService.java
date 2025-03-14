package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.database.entity.Address;
import org.example.database.entity.User;
import org.example.database.repository.UserRepository;
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

    public List<UserResponseDto> getUsers() {
        var users = userRepository.findAll();
        return users.stream().map(UserService::mapUserResponse).toList();
    }

    public Optional<UserResponseDto> getUser(Long id) {
        return userRepository.findById(id).map(UserService::mapUserResponse);
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) throws Exception {
        if (userRepository.findByEmail(userRequestDto.getEmail()) != null) {
            throw new Exception("User already exist");
        }
        var user = User.builder()
                .firstName(userRequestDto.getFirstName())
                .lastName(userRequestDto.getLastName())
                .email(userRequestDto.getEmail())
                .password(userRequestDto.getPassword())
                .role(userRequestDto.getRole())
                .build();

        user = userRepository.save(user);
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
                .build();
    }
}