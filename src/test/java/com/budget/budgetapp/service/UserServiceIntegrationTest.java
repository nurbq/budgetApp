package com.budget.budgetapp.service;

import com.budget.budgetapp.data.entity.UserEntity;
import com.budget.budgetapp.error.ConflictException;
import com.budget.budgetapp.error.NotFoundException;
import com.budget.budgetapp.model.dtos.UserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserServiceIntegrationTest {
    @Autowired
    UserService userService;


    @Test
    void getAllUsers() {
        int size = 2;

        List<UserDto> users = userService.getAllUsers();
        Assertions.assertThat(users.size()).isEqualTo(size);

        for (int i = 1; i <= size; i++) {
            userService.deleteUser((long) i);
        }
    }

    @Test
    void addUser() {
        UserDto userDto = new UserDto(10L, "UserTest", "userTest@gmail.com", "userTestPassword");
        userDto = userService.addUser(userDto);

        List<UserDto> users = userService.getAllUsers();

        Assertions.assertThat(users.size()).isEqualTo(1);
        Assertions.assertThat(userDto.getId()).isNotNull();
        Assertions.assertThat(userDto.getUsername()).isEqualTo("UserTest");

        userService.deleteUser(userDto.getId());
    }

    @Test
    void addUser_userAlreadyExists() {
        UserDto userDto = new UserDto(10L, "UserTest", "userTest@gmail.com", "userTestPassword");
        userDto = userService.addUser(userDto);

        UserDto finalUserDto = userDto;
        Assertions.assertThatThrownBy(() -> userService.addUser(finalUserDto))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("user already exists");

        userService.deleteUser(userDto.getId());
    }


    @Test
    void getUser_NotFound() {
        Assertions.assertThatThrownBy(() -> userService.getUserById(15L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("user not found");
    }

    @Test
    void updateUser() {
        UserDto userDto = new UserDto(10L, "UserTest", "userTest@gmail.com", "userTestPassword");
        userDto = userService.addUser(userDto);
        userDto.setUsername("WalterWhite");
        userDto = userService.updateUser(10L, userDto);

        Assertions.assertThat(userDto.getUsername()).isEqualTo("WalterWhite");

        userService.deleteUser(userDto.getId());
    }


    private List<UserEntity> getMockUsers(int count) {
        List<UserEntity> users = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            users.add(new UserEntity((long) i, "username" + i, "email" + i, "password" + i));
        }
        return users;
    }
}
