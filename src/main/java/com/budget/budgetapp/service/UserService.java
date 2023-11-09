package com.budget.budgetapp.service;

import com.budget.budgetapp.data.entity.UserEntity;
import com.budget.budgetapp.data.repository.RoleRepository;
import com.budget.budgetapp.data.repository.UserRepository;
import com.budget.budgetapp.error.BadRequestException;
import com.budget.budgetapp.error.ConflictException;
import com.budget.budgetapp.error.NotFoundException;
import com.budget.budgetapp.model.dtos.UserDto;
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
    private final RoleRepository roleRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToUserDto).collect(Collectors.toList());
    }

    public UserDto addUser(UserDto userDto) {
        Optional<UserEntity> userFromDb = userRepository.findByEmail(userDto.getEmail());

        if (userFromDb.isPresent()) {
            throw new ConflictException("user already exists");
        }

        UserEntity user = maptoUserEntity(userDto);
//        user.setCreatedDate(LocalDateTime.now());

//        Role role = new Role();
//        role.setName(Roles.USER);
//        role.setUser(user);

        LOGGER.info("UserService.findAddUser");
//        roleRepository.save(role);
        user = userRepository.save(user);

        return mapToUserDto(user);
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

    public UserDto updateUser(Long id, UserDto userDto) {
        if (!id.equals(userDto.getId())) {
            throw new BadRequestException("path variable must match incoming request id");
        }

        Optional<UserEntity> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isEmpty()) {
            throw new NotFoundException("user not found");
        }

        UserEntity userEntity = maptoUserEntity(userDto);
        userEntity.setId(existingUser.get().getId());
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

    private UserDto mapToUserDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
//                .createdDate(userEntity.getCreatedDate())
//                .authorities(userEntity.getAuthorities())
                .build();
    }

    private UserEntity maptoUserEntity(UserDto userDto) {
        return UserEntity.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
//                .createdDate(userDto.getCreatedDate())
//                .authorities(userDto.getAuthorities())
                .build();
    }
}
