package com.budget.budgetapp.service;

import com.budget.budgetapp.data.entity.Expense;
import com.budget.budgetapp.data.entity.ExpenseCategory;
import com.budget.budgetapp.data.entity.UserEntity;
import com.budget.budgetapp.data.repository.ExpenseCategoryRepository;
import com.budget.budgetapp.data.repository.ExpenseRepository;
import com.budget.budgetapp.data.repository.UserRepository;
import com.budget.budgetapp.model.dtos.ExpenseDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;

    @Transactional
    public ExpenseDto addExpense(ExpenseDto expenseDto, Long userId) throws Throwable {
        UserEntity userFromDb = userRepository.findById(userId)
                .orElseThrow((Supplier<Throwable>) () -> new EntityNotFoundException("User not found"));
        ExpenseCategory expenseCategory = expenseCategoryRepository.findByCategoryName(expenseDto.categoryName())
                .orElseThrow(() -> new EntityNotFoundException("Expense category not found: " + expenseDto.categoryName()));

        Expense expense = mapToExpenseEntity(expenseDto, userFromDb, expenseCategory);
        expenseRepository.save(expense);

        return mapToExpenseDto(expense, expenseCategory);
    }

    private ExpenseDto mapToExpenseDto(Expense expense, ExpenseCategory expenseCategory) {
        return new ExpenseDto(
                expenseCategory.getCategoryName(),
                expense.getDate(), expense.getAmount(), expense.getDescription());
    }

    private Expense mapToExpenseEntity(ExpenseDto expenseDto, UserEntity user, ExpenseCategory expenseCategory) {
        return new Expense(
                null, user, expenseCategory,
                expenseDto.date(), expenseDto.amount(), expenseDto.description());
    }
}
