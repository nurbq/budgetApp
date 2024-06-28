package com.budget.budgetapp.data.repository;


import com.budget.budgetapp.data.entity.UserEntity;
import com.budget.budgetapp.model.dtos.UserExpenseDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    //
//    @Query(value = """
//            select u.username, u.email,
//                   e.amount, e.description, ec.category_name
//            from expense e
//            join users u on e.user_id = u.id
//            join expense_category ec on ec.id = e.expense_category_id
//            where u.username = :username
//            """)
    @Query("""
            select new com.budget.budgetapp.model.dtos.UserExpenseDto(u.username, u.email, e.amount, e.description, ec.categoryName)
            from Expense e
            join e.user u
            join e.expenseCategory ec
            where u.username = :username
            """)
    List<UserExpenseDto> findUserExpenseDtoByUsername(@Param("username") String username);
}
