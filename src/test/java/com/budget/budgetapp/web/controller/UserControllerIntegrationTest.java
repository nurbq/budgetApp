package com.budget.budgetapp.web.controller;

import com.budget.budgetapp.data.entity.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

//    @Test
//    void getAllUsers() throws Exception {
//        this.mockMvc.perform(get("/users")).andExpect(status().isOk())
//                .andExpect(content().string(containsString("NURBA")))
//                .andExpect(content().string(containsString("FRANK")));
//    }


    @Test
    void getUser_NotFound() throws Exception {
        mockMvc.perform(get("/users/1")).andExpect(status().isNotFound());
    }

    @Test
    void addUser() throws Exception {
        UserEntity userEntity = new UserEntity(1L,"JohnDoe", "jdoe@test.com", "password");
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(userEntity);
        mockMvc.perform(post("/users/add").content(jsonString).contentType("application/json")).andExpect(status().isCreated())
                .andExpect(content().string(containsString("jdoe@test.com")));
    }

    @Test
    void addUser_exists() throws Exception {
        UserEntity userEntity = new UserEntity(1L, "JohnDoe", "jdoe@test.com", "password");
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(userEntity);
        mockMvc.perform(post("/users/add").content(jsonString).contentType("application/json"));
        mockMvc.perform(post("/users/add").content(jsonString).contentType("application/json")).andExpect(status().isConflict());
    }

    @Test
    void updateCustomer() throws Exception {
        UserEntity userEntity = new UserEntity(1L, "JohnDoe", "jdoe@test.com", "password");
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(userEntity);
        mockMvc.perform(post("/users/add").content(jsonString).contentType("application/json"));

        userEntity.setUsername("SalamDoe");
        userEntity.setPassword("testDoe");
        jsonString = mapper.writeValueAsString(userEntity);
        mockMvc.perform(put("/users/1").content(jsonString).contentType("application/json")).andExpect(status().isOk())
                .andExpect(content().string(containsString("SalamDoe")))
                .andExpect(content().string(containsString("testDoe")));
    }

    @Test
    void updateCustomer_badRequest() throws Exception {
        UserEntity userEntity = new UserEntity(1L, "JohnDoe", "jdoe@test.com", "password");
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(userEntity);
        mockMvc.perform(post("/users/add").content(jsonString).contentType("application/json"));

        userEntity.setUsername("SalamDoe");
        userEntity.setPassword("testDoe");
        jsonString = mapper.writeValueAsString(userEntity);
        mockMvc.perform(put("/users/2").content(jsonString).contentType("application/json")).andExpect(status().isBadRequest());
    }

    @Test
    void deleteCustomer() throws Exception {
        UserEntity userEntity = new UserEntity(1L, "JohnDoe", "jdoe@test.com", "password");
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(userEntity);
        mockMvc.perform(post("/users/add").content(jsonString).contentType("application/json"));

        mockMvc.perform(delete("/users/1")).andExpect(status().isResetContent());
    }

    @Test
    void deleteCustomer_userNotFound() throws Exception {
        mockMvc.perform(delete("/users/1")).andExpect(status().isNotFound());
    }
}
