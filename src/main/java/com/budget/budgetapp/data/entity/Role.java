package com.budget.budgetapp.data.entity;


import com.budget.budgetapp.model.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private Roles name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
