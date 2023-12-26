package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreate;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping
    public ResponseEntity<Collection<UserResponseDto>> getAllUsers() {
        log.info("Получен GET запрос на получение всех пользователей");
        return ResponseEntity.ok(userServiceImpl.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> addUser(@Valid @RequestBody UserCreate user, BindingResult bindingResult) throws Exception {
        log.info("Получен POST запрос на добавление нового пользователя");
        return ResponseEntity.ok(userServiceImpl.addUser(user, bindingResult));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserUpdateDto user, @PathVariable("id") Integer userId, BindingResult bindingResult) throws Exception {
        log.info("Получен Patch запрос на обновление пользователя с id = {}", userId);
        return ResponseEntity.ok(userServiceImpl.updateUser(user, userId, bindingResult));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("id") Integer userId) {
        log.info("Получен GET запрос на получение пользователя");
        return ResponseEntity.ok(userServiceImpl.getUserById(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable("id") Integer userId) {
        log.info("Запрос на удаление пользователя с id = {}", userId);
        return ResponseEntity.ok(userServiceImpl.deleteUserById(userId));
    }
}
