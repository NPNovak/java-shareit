package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient client;

    @PostMapping
    public ResponseEntity<Object> addItem(
            @Min(value = 1, message = "UserId должно быть больше 0")
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @Valid @RequestBody ItemDto itemDto) throws ValidationException {
        log.info("Получен POST запрос на добавление нового товара");
        return client.addItem(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(
            @Min(value = 1, message = "UserId должно быть больше 0")
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestBody ItemUpdateDto itemUpdateDto,
            @Min(value = 1, message = "ItemId должно быть больше 0")
            @PathVariable("id") Integer itemId) {
        log.info("Получен Patch запрос на обновление товара c id ={}", itemId);
        return client.updateItem(userId, itemId, itemUpdateDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(
            @Min(value = 1, message = "UserId должно быть больше 0")
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @Min(value = 1, message = "ItemId должно быть больше 0")
            @PathVariable("id") Integer itemId) {
        log.info("Получен GET запрос на получение товара");
        return client.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(
            @Min(value = 1, message = "UserId должно быть больше 0")
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @Min(value = 0, message = "from должно быть больше или равно 0")
            @RequestParam(defaultValue = "0") int from,
            @Min(value = 1, message = "size должно быть больше 0")
            @RequestParam(defaultValue = "20") int size) {
        log.info("Получен GET запрос на получение всех товаров");
        return client.getAllItems(userId, from, size);
    }

    @GetMapping("search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                         @Min(value = 0, message = "from должно быть больше или равно 0")
                                         @RequestParam(defaultValue = "0") int from,
                                         @Min(value = 1, message = "size должно быть больше 0")
                                         @RequestParam(defaultValue = "20") int size) {
        log.info("Получен GET запрос на получение всех товаров по названию");
        return client.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @Min(value = 1, message = "UserId должно быть больше 0")
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @Valid @RequestBody CommentDto commentDto,
            @Min(value = 1, message = "ItemId должно быть больше 0")
            @PathVariable("itemId") Integer itemId) throws ValidationException {
        log.info("Получен POST запрос на добавление нового коментария");
        return client.addComment(userId, itemId, commentDto);
    }
}
