package com.budget.budgetapp.model.mappers;

import com.budget.budgetapp.data.entity.UserEntity;
import com.budget.budgetapp.model.dtos.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(UserEntity userEntity);

    UserEntity toUserEntity(UserDto userDto);
}
