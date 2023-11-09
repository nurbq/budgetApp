package com.budget.budgetapp.repository;


import com.budget.budgetapp.data.entity.UserEntity;
import com.budget.budgetapp.data.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void savedUser() {
        UserEntity user = getUser();

        UserEntity savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getEmail()).isEqualTo("testEmail");
    }

    @Test
    public void getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        Assertions.assertThat(users.size()).isEqualTo(6);
        System.out.println(users.size());
    }


    private UserEntity getUser() {
        return new UserEntity(1L, "testUsername", "testEmail", "testPassword");
    }
}
