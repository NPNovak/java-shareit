package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemServiceImp;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemServiceImp itemService;

    @PostMapping
    public ResponseEntity<ItemResponseDto> addItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                   @Valid @RequestBody ItemDto itemDto, BindingResult bindingResult) throws ValidationException {
        log.info("Получен POST запрос на добавление нового товара");
        return ResponseEntity.ok(itemService.addItem(userId, itemDto, bindingResult));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemResponseDto> updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                      @RequestBody ItemUpdateDto itemUpdateDto, @PathVariable("id") Integer itemId) {
        log.info("Получен Patch запрос на обновление товара c id ={}", itemId);
        return ResponseEntity.ok(itemService.updateItem(userId, itemId, itemUpdateDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> getItem(@PathVariable("id") Integer itemId,
                                                   @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Получен GET запрос на получение товара");
        return ResponseEntity.ok(itemService.getItemById(itemId, userId));
    }

    @GetMapping
    public ResponseEntity<Collection<ItemResponseDto>> getAllItems(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestParam(defaultValue = "0") int from,
                                                                   @RequestParam(defaultValue = "20") int size) {
        log.info("Получен GET запрос на получение всех товаров");
        return ResponseEntity.ok(itemService.getAllItems(userId, from, size));
    }

    @GetMapping("search")
    public ResponseEntity<Collection<ItemResponseDto>> search(@RequestParam String text, @RequestParam(defaultValue = "0") int from,
                                                              @RequestParam(defaultValue = "20") int size) {
        log.info("Получен GET запрос на получение всех товаров по названию");
        return ResponseEntity.ok(itemService.search(text, from, size));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentResponseDto> addComment(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                         @RequestBody CommentDto commentDto,
                                                         @PathVariable("itemId") Integer itemId) throws ValidationException {
        log.info("Получен POST запрос на добавление нового коментария");
        return ResponseEntity.ok(itemService.addComment(userId, commentDto, itemId));
    }
}
