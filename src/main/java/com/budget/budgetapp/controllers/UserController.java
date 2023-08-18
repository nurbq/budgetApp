package com.budget.budgetapp.controllers;


import com.budget.budgetapp.models.responses.UserGetAllResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {



    @GetMapping("/all")
    public UserGetAllResponse userGetAll() {
        return new UserGetAllResponse();
    }
}
