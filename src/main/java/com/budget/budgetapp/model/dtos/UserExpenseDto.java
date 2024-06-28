package com.budget.budgetapp.model.dtos;

import java.math.BigDecimal;

public record UserExpenseDto(String username,
                             String email,
                             BigDecimal expenseAmount,
                             String expenseDescription,
                             String categoryName) {
}
