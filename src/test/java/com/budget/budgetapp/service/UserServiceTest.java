package com.budget.budgetapp.service;


import com.budget.budgetapp.data.entity.UserEntity;
import com.budget.budgetapp.data.repository.UserRepository;
import com.budget.budgetapp.exception.ConflictException;
import com.budget.budgetapp.exception.NotFoundException;
import com.budget.budgetapp.model.dtos.UserCreateDto;
import com.budget.budgetapp.model.dtos.UserDto;
import com.budget.budgetapp.model.mappers.UserMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;


    @Test
    void getAllUsers() {
        doReturn(getMockUsers(2)).when(userRepository).findAll();
        List<UserDto> userEntities = userService.getAllUsers();
        assertThat(userEntities.size()).isEqualTo(2);
    }

    @Test
    void addUser() {
        UserEntity userEntity = getUserEntity();
        UserCreateDto createUserDto = new UserCreateDto("testUsername", "testEmail", "testPassword");
        UserDto userDto = new UserDto(1L, "testUsername", "testEmail");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userMapper.toUserEntity(any(UserCreateDto.class))).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.toUserDto(userEntity)).thenReturn(userDto);

        userDto = userService.addUser(createUserDto);

        Assertions.assertThat(userDto).isNotNull();
        Assertions.assertThat(userDto.email()).isEqualTo(createUserDto.email());
    }

    @Test
    void addUser_userAlreadyExists() {
        UserEntity userEntity = getUserEntity();
        Optional<UserEntity> optional = Optional.of(userEntity);
        doReturn(optional).when(userRepository).findByEmail(userEntity.getEmail());
        UserCreateDto userDto = new UserCreateDto(userEntity.getUsername(), userEntity.getEmail(), userEntity.getPassword());

        Assertions.assertThatThrownBy(() -> userService.addUser(userDto))
                .isInstanceOf(ConflictException.class).hasMessageContaining("user already exists");
    }

    @Test
    void getUserById() {
        UserEntity userEntity = getUserEntity();
        UserDto userDto = new UserDto(null, "testUsername", "testEmail");
        Optional<UserEntity> optional = Optional.of(userEntity);
        doReturn(optional).when(userRepository).findById(anyLong());
        doReturn(userDto).when(userMapper).toUserDto(any(UserEntity.class));

        userDto = userService.getUserById(userEntity.getId());

        assertThat(userDto).isNotNull();
        assertThat(userDto.username()).isEqualTo("testUsername");

        verify(userRepository, times(1)).findById(userEntity.getId());
    }

    @Test
    void getUserById_notExists() {
        UserEntity userEntity = getUserEntity();
        Optional<UserEntity> optional = Optional.empty();
        doReturn(optional).when(userRepository).findById(userEntity.getId());

        Assertions.assertThatThrownBy(() -> {
            userService.getUserById(userEntity.getId());
        }).isInstanceOf(NotFoundException.class).hasMessageContaining("User not found, id: 1");
    }

    @Test
    void getUserByUsername() {
        UserEntity userEntity = getUserEntity();
        UserDto userDto = new UserDto(null, "testUsername", "testEmail");
        Optional<UserEntity> optional = Optional.of(userEntity);
        doReturn(optional).when(userRepository).findByUsername(userEntity.getUsername());
        doReturn(userDto).when(userMapper).toUserDto(any(UserEntity.class));
        userDto = userService.getByUsername(userEntity.getUsername());

        assertThat(userDto).isNotNull();
        assertThat(userDto.username()).isEqualTo("testUsername");
    }

    @Test
    void getUserByUsername_notExists() {
        UserEntity userEntity = getUserEntity();
        Optional<UserEntity> optionalUserEntity = Optional.empty();
        doReturn(optionalUserEntity).when(userRepository).findByUsername(userEntity.getUsername());

        Assertions.assertThatThrownBy(() -> {
            userService.getByUsername(userEntity.getUsername());
        }).isInstanceOf(NotFoundException.class).hasMessageContaining("User not found, username: testUsername");
    }

    @Test
    void getUserByEmail() {
        UserEntity userEntity = getUserEntity();
        UserDto userDto = new UserDto(null, "testUsername", "testEmail");
        Optional<UserEntity> optional = Optional.of(userEntity);
        doReturn(optional).when(userRepository).findByEmail(userEntity.getEmail());
        doReturn(userDto).when(userMapper).toUserDto(any(UserEntity.class));
        userDto = userService.getByEmail(userEntity.getEmail());

        assertThat(userDto).isNotNull();
        assertThat(userDto.email()).isEqualTo("testEmail");
    }

    @Test
    void getUserByEmail_NotExists() {
        UserEntity user = getUserEntity();
        Optional<UserEntity> optional = Optional.empty();

        doReturn(optional).when(userRepository).findByEmail(user.getEmail());

        Assertions.assertThatThrownBy(() -> {
            userService.getByEmail(user.getEmail());
        }).isInstanceOf(NotFoundException.class).hasMessageContaining("User not found, email: testEmail");
    }

    @Test
    void updateUser() {
        UserEntity user = getUserEntity();
        UserCreateDto userCreateDto = new UserCreateDto(user.getUsername(), user.getEmail(), user.getPassword());
        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail());

        doReturn(Optional.of(user)).when(userRepository).findById(anyLong());
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.toUserDto(any())).thenReturn(userDto);

        userDto = userService.updateUser(userDto.id(), userCreateDto);

        assertThat(userDto).isNotNull();
        assertThat(userDto.email()).isEqualTo("testEmail");
    }

    @Test
    void deleteUser() {
        UserEntity userEntity = getUserEntity();
        Optional<UserEntity> optional = Optional.of(userEntity);

        doReturn(optional).when(userRepository).findById(userEntity.getId());
        doNothing().when(userRepository).deleteById(userEntity.getId());

        userService.deleteUser(userEntity.getId());

        verify(userRepository, times(1)).deleteById(userEntity.getId());
    }

    @Test
    void deleteUser_notExists() {
        UserEntity userEntity = getUserEntity();
        Optional<UserEntity> optional = Optional.empty();
        when(userRepository.findById(userEntity.getId())).thenReturn(optional);

        Assertions.assertThatThrownBy(() -> userService.deleteUser(userEntity.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("user not found");
    }


    private UserEntity getUserEntity() {
        return new UserEntity(1L, "testUsername", "testEmail", "testPassword", LocalDateTime.now());
    }

    private List<UserEntity> getMockUsers(int count) {
        List<UserEntity> users = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            users.add(new UserEntity((long) i, "username" + i, "email" + i, "password" + i, LocalDateTime.now()));
        }
        return users;
    }
}
