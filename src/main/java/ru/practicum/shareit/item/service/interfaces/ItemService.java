package ru.practicum.shareit.item.service.interfaces;

import org.springframework.validation.BindingResult;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Collection;

public interface ItemService {
    public ItemResponseDto getItemById(Integer itemId);

    public Collection<ItemResponseDto> getAllItems(Integer userId);

    public ItemResponseDto addItem(Integer userId, ItemDto itemDto, BindingResult bindingResult) throws ValidationException;

    public ItemResponseDto updateItem(Integer userId, Integer itemId, ItemUpdateDto itemUpdateDto);

    public Collection<ItemResponseDto> search(String text);
}
