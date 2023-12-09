package com.budget.budgetapp.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    private String username;

    private String email;

    private String password;

//    private LocalDateTime createdDate;
//    private Set<Role> authorities;
}
