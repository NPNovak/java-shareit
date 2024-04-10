package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.RequestItemRequest;

import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient client;

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                 @PathVariable Integer requestId) {
        log.info("GET запрос - getItemRequest, UserId: " + userId + ", RequestId: " + requestId);
        return client.getItemRequest(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnerItemRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("GET запрос - getItemRequest, UserId: " + userId);
        return client.getOwnerItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequests(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                @RequestParam(defaultValue = "0") @Min(value = 0) int from,
                                                @RequestParam(defaultValue = "20") @Min(value = 0) int size) {
        log.info("GET запрос - getItemRequests, UserId: " + userId + ", from: " + from + ", size: " + size);
        return client.getItemRequests(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                         @RequestBody RequestItemRequest itemRequest) {
        log.info("POST запрос - addItemRequest, UserId: " + userId + ", ItemRequestRequest: " + itemRequest.toString());
        return client.addItemRequest(userId, itemRequest);
    }
}
