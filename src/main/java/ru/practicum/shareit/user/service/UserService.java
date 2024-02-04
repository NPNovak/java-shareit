package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.error.exception.ConflictException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.util.UserValidation;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserMapper mapper;
    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto addUser(UserCreateDto user, BindingResult bindingResult) throws Exception {
        UserValidation.validation(bindingResult);
        return mapper.toUserResponseDto(userRepository.save(mapper.toUser(user)));
    }

    @Transactional
    public UserResponseDto updateUser(UserUpdateDto user, Integer userId, BindingResult bindingResult) throws Exception {
        UserValidation.validation(bindingResult);
        User inUser = mapper.toUser(getUserById(userId));
        if (user.getEmail() != null) {
            checkEmail(user.getEmail(), userId);
            inUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            inUser.setName(user.getName());
        }
        return mapper.toUserResponseDto(userRepository.save(inUser));
    }

    public UserResponseDto getUserById(Integer userId) {
        return mapper.toUserResponseDto(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователя с такими id нет")));
    }

    @Transactional
    public UserResponseDto deleteUserById(Integer userId) {
        User inUser = mapper.toUser(getUserById(userId));
        if (getUserById(userId) == null) {
            throw new NotFoundException("Такого " + userId + " не существует");
        }
        userRepository.deleteById(userId);
        return mapper.toUserResponseDto(inUser);
    }

    public Collection<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(mapper::toUserResponseDto).collect(Collectors.toList());
    }

    private void checkEmail(String email, Integer userId) throws ValidationException {
        if (userRepository.findByEmailAndIdNot(email, userId) != null) {
            throw new ConflictException("Два пользователя не могут " +
                    "иметь одинаковый адрес электронной почты");
        }
    }
}
