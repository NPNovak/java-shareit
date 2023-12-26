package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemServiceImpl itemServiceImpl;

    @PostMapping
    public ResponseEntity<ItemResponseDto> addItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                   @Valid @RequestBody ItemDto itemDto, BindingResult bindingResult) throws ValidationException {
        log.info("Получен POST запрос на добавление нового товара");
        return ResponseEntity.ok(itemServiceImpl.addItem(userId, itemDto, bindingResult));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemResponseDto> updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                      @RequestBody ItemUpdateDto itemUpdateDto, @PathVariable("id") Integer itemId) {
        log.info("Получен Patch запрос на обновление товара c id ={}", itemId);
        return ResponseEntity.ok(itemServiceImpl.updateItem(userId, itemId, itemUpdateDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> getItem(@PathVariable("id") Integer itemId) {
        log.info("Получен GET запрос на получение товара");
        return ResponseEntity.ok(itemServiceImpl.getItemById(itemId));
    }

    @GetMapping
    public ResponseEntity<Collection<ItemResponseDto>> getAllItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Получен GET запрос на получение всех товаров");
        return ResponseEntity.ok(itemServiceImpl.getAllItems(userId));
    }

    @GetMapping("search")
    public ResponseEntity<Collection<ItemResponseDto>> search(@RequestParam String text) {
        log.info("Получен GET запрос на получение всех товаров по названию");
        return ResponseEntity.ok(itemServiceImpl.search(text));
    }
}
