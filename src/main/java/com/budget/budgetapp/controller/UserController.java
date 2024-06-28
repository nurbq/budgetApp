package com.budget.budgetapp.controller;


import com.budget.budgetapp.model.dtos.UserCreateDto;
import com.budget.budgetapp.model.dtos.UserDto;
import com.budget.budgetapp.model.dtos.UserExpenseDto;
import com.budget.budgetapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }


    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody UserCreateDto userCreateDto) {
        return userService.addUser(userCreateDto);
    }

    @PutMapping("/{id}")
    public UserDto editUser(@PathVariable("id") Long id, @RequestBody UserCreateDto createUserDto) {
        return userService.updateUser(id, createUserDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{username}/expense")
    public List<UserExpenseDto> getUserExpense(@PathVariable String username) {
        return userService.getUserExpenseByUsername(username);
    }
}
