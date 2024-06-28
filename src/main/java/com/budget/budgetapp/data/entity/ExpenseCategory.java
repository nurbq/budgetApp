package com.budget.budgetapp.data.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "expense_category")
public class ExpenseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "category_name", unique = true, length = 150)
    private String categoryName;

    @OneToMany(mappedBy = "expenseCategory", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Expense> expenses;
}
