package com.budget.budgetapp.model.mappers;

import com.budget.budgetapp.data.entity.UserEntity;
import com.budget.budgetapp.model.dtos.UserCreateDto;
import com.budget.budgetapp.model.dtos.UserDto;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface UserMapper {
    UserDto toUserDto(UserEntity userEntity);

    UserEntity toUserEntity(UserDto userDto);

    @Mapping(target = "createdDate", expression = "java(LocalDateTime.now())")
    UserEntity toUserEntity(UserCreateDto userCreateDto);
}
