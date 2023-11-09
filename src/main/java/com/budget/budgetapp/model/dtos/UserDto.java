package com.budget.budgetapp.model.dtos;

import com.budget.budgetapp.data.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

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
