package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreate;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.interfaces.UserService;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.util.UserValidation;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper mapper;

    public UserResponseDto addUser(UserCreate user, BindingResult bindingResult) throws Exception {
        UserValidation.validation(bindingResult);
        return mapper.toUserResponse(userStorage.addUser(mapper.toUser(user)));
    }

    public UserResponseDto updateUser(UserUpdateDto user, Integer userId, BindingResult bindingResult) throws Exception {
        UserValidation.validation(bindingResult);
        return mapper.toUserResponse(userStorage.updateUser(mapper.toUser(user), userId));
    }

    public UserResponseDto getUserById(Integer userId) {
        return mapper.toUserResponse(userStorage.getUser(userId));
    }

    public UserResponseDto deleteUserById(Integer userId) {
        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("Такого " + userId + " не существует");
        }
        return mapper.toUserResponse(userStorage.deleteUser(userId));
    }

    public Collection<UserResponseDto> getAllUsers() {
        return userStorage.getAllUsers().stream().map(mapper::toUserResponse).collect(Collectors.toList());
    }
}
