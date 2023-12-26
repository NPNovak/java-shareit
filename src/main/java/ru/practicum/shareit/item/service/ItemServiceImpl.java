package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.interfaces.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.util.ItemValidation;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final ItemMapper mapper;
    private final UserStorage userStorage;

    public ItemResponseDto getItemById(Integer itemId) {
        return mapper.toItemResponse(itemStorage.getItem(itemId));
    }

    public Collection<ItemResponseDto> getAllItems(Integer userId) {
        return itemStorage.getAllItems(userId).stream().map(mapper::toItemResponse).collect(Collectors.toList());
    }

    public ItemResponseDto addItem(Integer userId, ItemDto itemDto, BindingResult bindingResult) throws ValidationException {
        ItemValidation.validation(bindingResult);
        return mapper.toItemResponse(itemStorage.addItem(mapper.toItem(itemDto), userStorage.getUser(userId)));
    }

    public ItemResponseDto updateItem(Integer userId, Integer itemId, ItemUpdateDto itemUpdateDto) {
        return mapper.toItemResponse(itemStorage.updateItem(mapper.toItem(itemUpdateDto), userId, itemId));
    }

    public Collection<ItemResponseDto> search(String text) {
        return itemStorage.search(text).stream().map(mapper::toItemResponse).collect(Collectors.toList());
    }
}
