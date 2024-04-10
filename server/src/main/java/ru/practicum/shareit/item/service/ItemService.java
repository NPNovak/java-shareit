package ru.practicum.shareit.item.service;

import org.springframework.validation.BindingResult;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;

import java.util.Collection;

public interface ItemService {
    ItemResponseDto addItem(Integer userId, ItemDto itemDto, BindingResult bindingResult) throws ValidationException;

    ItemResponseDto updateItem(Integer userId, Integer itemId, ItemUpdateDto itemUpdate);

    ItemResponseDto getItemById(Integer itemId, Integer userId);

    Collection<ItemResponseDto> getAllItems(Integer userId, int from, int size);

    Collection<ItemResponseDto> search(String text, int from, int size);

    CommentResponseDto addComment(Integer userId, CommentDto commentDto, Integer itemId) throws ValidationException;

    void addLastAndNextBooking(ItemResponseDto item, Integer ownerId);
}
