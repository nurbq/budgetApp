package com.budget.budgetapp.controller;


import com.budget.budgetapp.model.dtos.ExpenseDto;
import com.budget.budgetapp.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/user/{userId}")
    public ExpenseDto createExpense(@RequestBody ExpenseDto expenseDto,@PathVariable Long userId) throws Throwable {
        return expenseService.addExpense(expenseDto, userId);
    }
}
