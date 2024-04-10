package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.RequestItemRequest;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient client;

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(
            @Min(value = 1, message = "UserId должно быть больше 0")
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @Min(value = 1, message = "RequestId должно быть больше 0")
            @PathVariable Integer requestId) {
        log.info("GET запрос - getItemRequest, UserId: " + userId + ", RequestId: " + requestId);
        return client.getItemRequest(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnerItemRequests(
            @Min(value = 1, message = "UserId должно быть больше 0")
            @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("GET запрос - getItemRequest, UserId: " + userId);
        return client.getOwnerItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequests(
            @Min(value = 1, message = "UserId должно быть больше 0")
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @Min(value = 0, message = "from должно быть больше или равно 0")
            @RequestParam(defaultValue = "0") @Min(value = 0) int from,
            @Min(value = 1, message = "size должно быть больше 0")
            @RequestParam(defaultValue = "20") @Min(value = 0) int size) {
        log.info("GET запрос - getItemRequests, UserId: " + userId + ", from: " + from + ", size: " + size);
        return client.getItemRequests(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addItemRequest(
            @Min(value = 1, message = "UserId должно быть больше 0")
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @Valid @RequestBody RequestItemRequest itemRequest) {
        log.info("POST запрос - addItemRequest, UserId: " + userId + ", ItemRequestRequest: " + itemRequest.toString());
        return client.addItemRequest(userId, itemRequest);
    }
}
