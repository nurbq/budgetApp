package com.budget.budgetapp.service;

import com.budget.budgetapp.data.entity.UserEntity;
import com.budget.budgetapp.data.repository.UserRepository;
import com.budget.budgetapp.exception.ConflictException;
import com.budget.budgetapp.exception.NotFoundException;
import com.budget.budgetapp.model.dtos.UserCreateDto;
import com.budget.budgetapp.model.dtos.UserDto;
import com.budget.budgetapp.model.dtos.UserExpenseDto;
import com.budget.budgetapp.model.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto addUser(UserCreateDto userCreateDto) {
        userRepository.findByEmail(userCreateDto.email())
                .ifPresent(user -> {
                    throw new ConflictException("user already exists");
                });

        UserEntity user = userMapper.toUserEntity(userCreateDto);
//        user.setCreatedDate(LocalDateTime.now());

//        Role role = new Role();
//        role.setName(Roles.USER);
//        role.setUser(user);

//        roleRepository.save(role);
        user = userRepository.save(user);

        return userMapper.toUserDto(user);
    }


    public UserDto getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("User not found, id: " + id));
    }

    public UserDto getByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("User not found, username: " + username));
    }

    public UserDto getByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("User not found, email: " + email));
    }

    public UserDto updateUser(Long id, UserCreateDto userDto) {

        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        UserEntity userEntity = userMapper.toUserEntity(userDto);
        userEntity = userRepository.save(userEntity);

        return userMapper.toUserDto(userEntity);
    }

    public void deleteUser(Long id) {
        userRepository.findById(id)
                .ifPresentOrElse(
                        userEntity -> userRepository.deleteById(id),
                        () -> {
                            throw new NotFoundException("user not found");
                        }
                );
    }

    public List<UserExpenseDto> getUserExpenseByUsername(String username) {
        return userRepository.findUserExpenseDtoByUsername(username);
    }
}
