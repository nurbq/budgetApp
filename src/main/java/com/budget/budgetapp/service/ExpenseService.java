package com.budget.budgetapp.service;

import com.budget.budgetapp.data.entity.Expense;
import com.budget.budgetapp.data.repository.ExpenseRepository;
import com.budget.budgetapp.model.dtos.ExpenseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseDto addExpense(ExpenseDto expenseDto) {
        Expense expense = mapToExpenseEntity(expenseDto);

        expense = expenseRepository.save(expense);

        return mapToExpenseDto(expense);
    }

    private ExpenseDto mapToExpenseDto(Expense expense) {
        return new ExpenseDto(
                expense.getId(), expense.getUser(), expense.getExpenseCategory(),
                expense.getDate(), expense.getAmount(), expense.getDescription());
    }

    private Expense mapToExpenseEntity(ExpenseDto expenseDto) {
        return new Expense(
                expenseDto.id(), expenseDto.user(), expenseDto.expenseCategory(),
                expenseDto.date(), expenseDto.amount(), expenseDto.description());
    }
}
