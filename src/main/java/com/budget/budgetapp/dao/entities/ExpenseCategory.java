package com.budget.budgetapp.dao.entities;


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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "expenseCategory", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Expense> expenses;
}
