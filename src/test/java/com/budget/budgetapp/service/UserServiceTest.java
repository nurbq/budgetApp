package com.budget.budgetapp.service;


import com.budget.budgetapp.data.entity.UserEntity;
import com.budget.budgetapp.data.repository.UserRepository;
import com.budget.budgetapp.error.ConflictException;
import com.budget.budgetapp.error.NotFoundException;
import com.budget.budgetapp.model.dtos.UserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

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
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        UserDto userDto = new UserDto(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(), userEntity.getPassword());
        userDto = userService.addUser(userDto);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getEmail()).isEqualTo("testEmail");
    }

    @Test
    void addUser_userAlreadyExists() {
        UserEntity userEntity = getUserEntity();
        Optional<UserEntity> optional = Optional.of(userEntity);
        doReturn(optional).when(userRepository).findByEmail(userEntity.getEmail());
        UserDto userDto = new UserDto(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(), userEntity.getPassword());

        Assertions.assertThatThrownBy(() -> {
            userService.addUser(userDto);
        }).isInstanceOf(ConflictException.class).hasMessageContaining("user already exists");
    }

    @Test
    void getUserById() {
        UserEntity userEntity = getUserEntity();
        Optional<UserEntity> optional = Optional.of(userEntity);
        doReturn(optional).when(userRepository).findById(userEntity.getId());

        UserDto userDto = userService.getUserById(userEntity.getId());

        assertThat(userDto).isNotNull();
        assertThat(userDto.getUsername()).isEqualTo("testUsername");
    }

    @Test
    void getUserById_notExists() {
        UserEntity userEntity = getUserEntity();
        Optional<UserEntity> optional = Optional.empty();
        doReturn(optional).when(userRepository).findById(userEntity.getId());

        Assertions.assertThatThrownBy(() -> {
            userService.getUserById(userEntity.getId());
        }).isInstanceOf(NotFoundException.class).hasMessageContaining("user not found");
    }

    @Test
    void getUserByUsername() {
        UserEntity userEntity = getUserEntity();
        Optional<UserEntity> optional = Optional.of(userEntity);
        doReturn(optional).when(userRepository).findByUsername(userEntity.getUsername());
        UserDto userDto = userService.getByUsername(userEntity.getUsername());

        assertThat(userDto).isNotNull();
        assertThat(userDto.getUsername()).isEqualTo("testUsername");
    }

    @Test
    void getUserByUsername_notExists() {
        UserEntity userEntity = getUserEntity();
        Optional<UserEntity> optionalUserEntity = Optional.empty();
        doReturn(optionalUserEntity).when(userRepository).findByUsername(userEntity.getUsername());

        Assertions.assertThatThrownBy(() -> {
            userService.getByUsername(userEntity.getUsername());
        }).isInstanceOf(NotFoundException.class).hasMessageContaining("user not found");
    }

    @Test
    void getUserByEmail() {
        UserEntity userEntity = getUserEntity();
        Optional<UserEntity> optional = Optional.of(userEntity);
        doReturn(optional).when(userRepository).findByEmail(userEntity.getEmail());
        UserDto userDto = userService.getByEmail(userEntity.getEmail());

        assertThat(userDto).isNotNull();
        assertThat(userDto.getEmail()).isEqualTo("testEmail");
    }

    @Test
    void getUserByEmail_NotExists() {
        UserEntity user = getUserEntity();
        Optional<UserEntity> optional = Optional.empty();
        doReturn(optional).when(userRepository).findByEmail(user.getEmail());

        Assertions.assertThatThrownBy(() -> {
            userService.getByEmail(user.getEmail());
        }).isInstanceOf(NotFoundException.class).hasMessageContaining("user not found");
    }

    @Test
    void updateUser() {
        UserEntity user = getUserEntity();
        Optional<UserEntity> optional = Optional.of(user);
        doReturn(optional).when(userRepository).findByEmail(user.getEmail());
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
        userDto = userService.updateUser(userDto.getId(), userDto);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getEmail()).isEqualTo("testEmail");
    }

    @Test
    void updateUser_NotExists() {
        UserEntity userEntity = getUserEntity();
        Optional<UserEntity> optional = Optional.empty();
        doReturn(optional).when(userRepository).findByEmail(userEntity.getEmail());
        UserDto userDto = new UserDto(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(), userEntity.getPassword());

        Assertions.assertThatThrownBy(() -> userService.updateUser(userDto.getId(), userDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("user not found");
    }

    @Test
    void deleteUser() {
        UserEntity userEntity = getUserEntity();
        Optional<UserEntity> optional = Optional.of(userEntity);

        doReturn(optional).when(userRepository).findById(userEntity.getId());
        doNothing().when(userRepository).deleteById(userEntity.getId());

        userService.deleteUser(userEntity.getId());
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
        return new UserEntity(1L, "testUsername", "testEmail", "testPassword");
    }

    private List<UserEntity> getMockUsers(int count) {
        List<UserEntity> users = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            users.add(new UserEntity((long) i, "username" + i, "email" + i, "password" + i));
        }
        return users;
    }

}
