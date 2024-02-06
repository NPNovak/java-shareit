package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserServiceImp;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImp userService;

    @GetMapping
    public ResponseEntity<Collection<UserResponseDto>> getAllUsers() {
        log.info("Получен GET запрос на получение всех пользователей");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> addUser(@Valid @RequestBody UserCreateDto user, BindingResult bindingResult) throws Exception {
        log.info("Получен POST запрос на добавление нового пользователя");
        return ResponseEntity.ok(userService.addUser(user, bindingResult));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserUpdateDto user, @PathVariable("id") Integer userId, BindingResult bindingResult) throws Exception {
        log.info("Получен Patch запрос на обновление пользователя с id = {}", userId);
        return ResponseEntity.ok(userService.updateUser(user, userId, bindingResult));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("id") Integer userId) {
        log.info("Получен GET запрос на получение пользователя");
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable("id") Integer userId) {
        log.info("Запрос на удаление пользователя с id = {}", userId);
        return ResponseEntity.ok(userService.deleteUserById(userId));
    }
}
