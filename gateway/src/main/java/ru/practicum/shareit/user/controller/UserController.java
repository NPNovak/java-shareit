package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.OnCreate;
import ru.practicum.shareit.user.OnUpdate;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient client;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Получен GET запрос на получение всех пользователей");
        return client.getAllUsers();
    }

    @PostMapping
    @Validated(OnCreate.class)
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserCreateDto user) {
        log.info("Получен POST запрос на добавление нового пользователя");
        return client.addUser(user);
    }

    @PatchMapping("/{id}")
    @Validated(OnUpdate.class)
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserUpdateDto user, @PathVariable("id") Integer userId) {
        log.info("Получен Patch запрос на обновление пользователя с id = {}", userId);
        return client.updateUser(user, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") Integer userId) {
        log.info("Получен GET запрос на получение пользователя");
        return client.getUserById(userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Integer userId) {
        log.info("Запрос на удаление пользователя с id = {}", userId);
        return client.deleteUserById(userId);
    }
}
