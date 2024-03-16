package ru.practicum.shareit.user.service;

import org.springframework.validation.BindingResult;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.Collection;


public interface UserService {

    UserResponseDto addUser(UserCreateDto user, BindingResult bindingResult) throws Exception;

    UserResponseDto updateUser(UserUpdateDto user, Integer userId, BindingResult bindingResult) throws Exception;

    UserResponseDto getUserById(Integer userId);

    UserResponseDto deleteUserById(Integer userId);

    Collection<UserResponseDto> getAllUsers();

    void checkEmail(String email, Integer userId) throws ValidationException;
}
