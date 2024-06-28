package com.budget.budgetapp.service;

import com.budget.budgetapp.data.entity.UserEntity;
import com.budget.budgetapp.data.repository.UserRepository;
import com.budget.budgetapp.exception.ConflictException;
import com.budget.budgetapp.exception.NotFoundException;
import com.budget.budgetapp.model.dtos.UserCreateDto;
import com.budget.budgetapp.model.dtos.UserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Testcontainers
public class UserServiceIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse(
            "postgres:15-alpine"));

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }


    @Test
    void getAllUsers() {
        int size = 2;
        for (int i = 1; i <= size; i++) {
            userRepository.save(new UserEntity((long) i, "testUsername" + i, "email" + i, "password" + i, LocalDateTime.now()));
        }

        List<UserDto> users = userService.getAllUsers();
        Assertions.assertThat(users.size()).isEqualTo(size);

    }

    @Test
    void addUser() {
        UserCreateDto userCreateDto = new UserCreateDto("testUsername", "testEmail", "testPassword");

        UserDto userDto = userService.addUser(userCreateDto);

        Assertions.assertThat(userDto).isNotNull();
        Assertions.assertThat(userDto.email()).isEqualTo(userCreateDto.email());
    }

    @Test
    void addUser_userAlreadyExists() {
        UserCreateDto userCreateDto = new UserCreateDto("testUsername", "testEmail", "testPassword");
        userService.addUser(userCreateDto);

        Assertions.assertThatThrownBy(() -> userService.addUser(userCreateDto))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("user already exists");
    }


    @Test
    void getUser_NotFound() {
        Assertions.assertThatThrownBy(() -> userService.getUserById(15L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User not found, id: " + 15);
    }

    @Test
    void successful_get_user_by_email() {
        UserEntity user = getUserEntity();
        userRepository.save(user);

        UserDto found = userService.getByEmail("jane@test.com");

        Assertions.assertThat(found.email()).isEqualTo(user.getEmail());
    }

    @Test
    void successful_get_user_by_username() {
        UserEntity user = getUserEntity();
        userRepository.save(user);

        UserDto found = userService.getByUsername("Jane Doe");

        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.username()).isEqualTo(user.getUsername());
    }

    @Test
    void updateUser() {
        UserEntity user = getUserEntity();
        user = userRepository.save(user);

        UserCreateDto userCreateDto = new UserCreateDto("Walter", "WalterWhite", "walter@gmail.com");

        UserDto userDto = userService.updateUser(user.getId(), userCreateDto);

        Assertions.assertThat(userDto.email()).isEqualTo("WalterWhite");
    }

    private UserEntity getUserEntity() {
        UserEntity user = new UserEntity();
        user.setUsername("Jane Doe");
        user.setEmail("jane@test.com");
        user.setPassword("test");
        user.setCreatedDate(LocalDateTime.now());
        return user;
    }
}
