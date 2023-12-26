package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserCreate;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreate userCreate);

    UserCreate toUserCreate(User user);

    User toUser(UserUpdateDto userCreate);

    UserCreate toUserUpdate(User user);

    User toUser(UserResponseDto userResponseDto);

    UserResponseDto toUserResponse(User user);
}
