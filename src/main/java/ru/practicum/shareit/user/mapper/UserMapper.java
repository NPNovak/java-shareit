package ru.practicum.shareit.user.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, builder = @Builder(disableBuilder = true))
public interface UserMapper {

    User toUser(UserCreateDto userCreateDto);

    UserCreateDto toUserCreateDto(User user);

    User toUser(UserUpdateDto userCreateDto);

    UserCreateDto toUserUpdateDto(User user);

    User toUser(UserResponseDto userResponseDto);

    UserResponseDto toUserResponseDto(User user);
}
