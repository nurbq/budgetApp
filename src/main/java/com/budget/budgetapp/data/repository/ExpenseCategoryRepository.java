package com.budget.budgetapp.data.repository;

import com.budget.budgetapp.data.entity.ExpenseCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {

    Optional<ExpenseCategory> findByCategoryName(String name);
}
