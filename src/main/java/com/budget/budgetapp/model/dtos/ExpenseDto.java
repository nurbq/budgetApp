package com.budget.budgetapp.model.dtos;

import com.budget.budgetapp.data.entity.ExpenseCategory;
import com.budget.budgetapp.data.entity.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record ExpenseDto(Long id, UserEntity user, ExpenseCategory expenseCategory, LocalDateTime date,
                         BigDecimal amount, String description) {

}
