package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.RequestedItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemRequest;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {
    ItemRequest toItemRequest(RequestItemRequest requestItemRequest);

    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);

    ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<RequestedItemRequest> items);
}
