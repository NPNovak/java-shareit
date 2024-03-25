package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService service;

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                         @PathVariable Integer requestId) {
        log.info("GET запрос - getItemRequest, UserId: " + userId + ", RequestId: " + requestId);
        return service.getItemRequest(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestDto> getOwnerItemRequests(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("GET запрос - getItemRequest, UserId: " + userId);
        return service.getOwnerItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                @RequestParam(defaultValue = "0") @Min(value = 0) int from,
                                                @RequestParam(defaultValue = "20") @Min(value = 0) int size) {
        log.info("GET запрос - getItemRequests, UserId: " + userId + ", from: " + from + ", size: " + size);
        return service.getItemRequests(userId, from, size);
    }

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                         @RequestBody RequestItemRequest itemRequest) {
        log.info("POST запрос - addItemRequest, UserId: " + userId + ", ItemRequestRequest: " + itemRequest.toString());
        return service.addItemRequest(userId, itemRequest);
    }
}
