package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.entity.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item toItem(ItemDto itemDto);

    ItemDto toItemDto(Item item);

    Item toItem(ItemUpdateDto itemUpdateDto);

    ItemUpdateDto toItemUpdate(Item item);

    Item toItem(ItemResponseDto itemResponseDto);

    ItemResponseDto toItemResponseDto(Item item);
}
