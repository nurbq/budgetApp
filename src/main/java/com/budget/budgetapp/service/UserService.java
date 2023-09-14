package com.budget.budgetapp.service;

import com.budget.budgetapp.data.entity.UserEntity;
import com.budget.budgetapp.data.repository.UserRepository;
import com.budget.budgetapp.error.ConflictException;
import com.budget.budgetapp.error.NotFoundException;
import com.budget.budgetapp.model.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToUserDto).collect(Collectors.toList());
    }

    private UserDto mapToUserDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .build();
    }

    public UserDto addUser(UserDto userDto) {
        Optional<UserEntity> userFromDb = userRepository.findByEmail(userDto.getEmail());

        if (userFromDb.isPresent()) {
            throw new ConflictException("user already exists");
        }

        UserEntity user = maptoUserEntity(userDto);
        user = userRepository.save(user);

        return mapToUserDto(user);
    }

    private UserEntity maptoUserEntity(UserDto userDto) {
        return new UserEntity(userDto.getId(), userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
    }

    public UserDto getUserById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);

        if (userEntity.isEmpty()) {
            throw new NotFoundException("user not found id: " + id);
        }

        return mapToUserDto(userEntity.get());
    }

    public UserDto getByUsername(String username) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);

        if (userEntity.isEmpty()) {
            throw new NotFoundException("user not found username: " + username);
        }

        return mapToUserDto(userEntity.get());
    }

    public UserDto getByEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new NotFoundException("user not found email: " + email);
        }


        return mapToUserDto(user.get());
    }

    public UserDto updateUser(UserDto userDto) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isEmpty()) {
            throw new NotFoundException("user not found");
        }

        UserEntity userEntity = maptoUserEntity(userDto);
        userEntity = userRepository.save(userEntity);

        return mapToUserDto(userEntity);
    }

    public void deleteUser(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isEmpty()) {
            throw new NotFoundException("user not found");
        }

        userRepository.deleteById(id);
    }
}
