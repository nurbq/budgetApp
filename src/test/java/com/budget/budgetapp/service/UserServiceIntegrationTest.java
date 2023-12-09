package com.budget.budgetapp.service;

import com.budget.budgetapp.data.entity.UserEntity;
import com.budget.budgetapp.data.repository.UserRepository;
import com.budget.budgetapp.error.ConflictException;
import com.budget.budgetapp.error.NotFoundException;
import com.budget.budgetapp.model.dtos.UserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    void getAllUsers() {
        int size = 2;


        for (int i = 1; i <= size; i++) {
            userRepository.save(new UserEntity((long) i, "testUsername" + i, "email" + i, "password" + i));
        }

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
    void successful_get_user_by_email() {
        UserEntity user = new UserEntity();
        user.setUsername("Jane Doe");
        user.setEmail("jane@test.com");
        user.setPassword("test");
        userRepository.save(user);

        UserDto found = userService.getByEmail("jane@test.com");

        Assertions.assertThat("Jane Doe").isEqualTo(found.getUsername());

        userRepository.delete(user);
    }

    @Test
    void successful_get_user_by_username() {
        UserEntity user = new UserEntity();
        user.setUsername("John Doe");
        user.setEmail("john@test.com");
        user.setPassword("test");
        userRepository.save(user);

        UserDto found = userService.getByUsername("John Doe");

        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat("john@test.com").isEqualTo(found.getEmail());

        userRepository.delete(user);
    }

    @Test
    void updateUser() {
        UserDto userDto = new UserDto(1L, "UserTest", "userTest@gmail.com", "userTestPassword");
        userDto = userService.addUser(userDto);
        userDto.setUsername("WalterWhite");


        userDto = userService.updateUser(1L, userDto);

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
