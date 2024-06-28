//package com.budget.budgetapp.testconfig;
//
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.testcontainers.containers.PostgreSQLContainer;
//
//@TestConfiguration
//public class TestContainerConfig {
//
//    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgresql:latest")
//            .withDatabaseName("testDb")
//            .withUsername("testuser")
//            .withPassword("testpass");
//
//
//    @Bean
//    public static PostgreSQLContainer<?> postgreSQLContainer() {
//        return postgresContainer;
//    }
//}
