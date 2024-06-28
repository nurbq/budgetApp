package com.budget.budgetapp.model.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;


public record ExpenseDto(String categoryName,
                         @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                         LocalDateTime date,
                         BigDecimal amount,
                         String description) {

}
