package com.budget.budgetapp.service;

import com.budget.budgetapp.data.entity.UserEntity;
import com.budget.budgetapp.data.repository.UserRepository;
import com.budget.budgetapp.error.BadRequestException;
import com.budget.budgetapp.error.ConflictException;
import com.budget.budgetapp.error.NotFoundException;
import com.budget.budgetapp.model.dtos.UserDto;
import com.budget.budgetapp.model.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    public UserDto addUser(UserDto userDto) {
        Optional<UserEntity> userFromDb = userRepository.findByEmail(userDto.getEmail());

        if (userFromDb.isPresent()) {
            throw new ConflictException("user already exists");
        }

        UserEntity user = userMapper.toUserEntity(userDto);
//        user.setCreatedDate(LocalDateTime.now());

//        Role role = new Role();
//        role.setName(Roles.USER);
//        role.setUser(user);

        LOGGER.info("UserService.findAddUser");
//        roleRepository.save(role);
        user = userRepository.save(user);

        return userMapper.toDto(user);
    }


    public UserDto getUserById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);

        if (userEntity.isEmpty()) {
            throw new NotFoundException("user not found id: " + id);
        }

        return userMapper.toDto(userEntity.get());
    }

    public UserDto getByUsername(String username) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);

        if (userEntity.isEmpty()) {
            throw new NotFoundException("user not found username: " + username);
        }

        return userMapper.toDto(userEntity.get());
    }

    public UserDto getByEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new NotFoundException("user not found email: " + email);
        }
        return userMapper.toDto(user.get());
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        if (!id.equals(userDto.getId())) {
            throw new BadRequestException("path variable must match incoming request id");
        }

        Optional<UserEntity> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isEmpty()) {
            throw new NotFoundException("user not found");
        }

        UserEntity userEntity = userMapper.toUserEntity(userDto);
        userEntity.setId(existingUser.get().getId());
        userEntity = userRepository.save(userEntity);

        return userMapper.toDto(userEntity);
    }

    public void deleteUser(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isEmpty()) {
            throw new NotFoundException("user not found");
        }

        userRepository.deleteById(id);
    }
}
