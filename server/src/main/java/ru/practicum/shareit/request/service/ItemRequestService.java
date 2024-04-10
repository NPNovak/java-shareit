package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto getItemRequest(Integer userId, Integer requestId);

    List<ItemRequestDto> getOwnerItemRequests(Integer userId);

    List<ItemRequestDto> getItemRequests(Integer userId, Integer from, Integer size);

    ItemRequestDto addItemRequest(Integer userId, RequestItemRequest requestedItemRequest);
}
