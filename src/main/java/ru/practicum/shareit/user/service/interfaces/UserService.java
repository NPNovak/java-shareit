package ru.practicum.shareit.user.service.interfaces;

import org.springframework.validation.BindingResult;
import ru.practicum.shareit.user.dto.UserCreate;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.Collection;

public interface UserService {
    public UserResponseDto addUser(UserCreate user, BindingResult bindingResult) throws Exception;

    public UserResponseDto updateUser(UserUpdateDto user, Integer userId, BindingResult bindingResult) throws Exception;

    public UserResponseDto getUserById(Integer userId);

    public UserResponseDto deleteUserById(Integer userId);

    public Collection<UserResponseDto> getAllUsers();
}
