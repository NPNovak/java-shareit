package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemRequest;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.error.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper mapper;

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemRequestDto getItemRequest(Integer userId, Integer requestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User с заданным id = " + userId + " не найден"));
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("ItemRequest с заданным id = " + requestId + " не найден"));

        return createItemRequestDtoWithRequestedItems(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getOwnerItemRequests(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User с заданным id = " + userId + " не найден"));

        return itemRequestRepository.findByRequesterIdOrderByCreatedDesc(userId)
                .stream()
                .map(this::createItemRequestDtoWithRequestedItems)
                .collect(toList());
    }

    @Override
    public List<ItemRequestDto> getItemRequests(Integer userId, Integer from, Integer size) {
        if (from < 0 || size < 0) {
            throw new ValidationException("From and size must be greater than or equal to 0");
        }

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User с заданным id = " + userId + " не найден"));

        Pageable sortedByCreated = PageRequest.of(from / size, size, Sort.by("created").descending());

        return itemRequestRepository.findByRequesterIdNot(userId, sortedByCreated)
                .stream()
                .map(this::createItemRequestDtoWithRequestedItems)
                .collect(toList());
    }

    @Override
    @Transactional
    public ItemRequestDto addItemRequest(Integer userId, RequestItemRequest requestItemRequest) {
        if (requestItemRequest.getDescription() == null) {
            throw new ValidationException("Description cant be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User с заданным id = " + userId + " не найден"));

        ItemRequest itemRequest = mapper.toItemRequest(requestItemRequest);
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());

        return mapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    private ItemRequestDto createItemRequestDtoWithRequestedItems(ItemRequest itemRequest) {
        return mapper.toItemRequestDto(itemRequest,
                itemRepository
                        .findByRequestIdOrderByRequestCreatedDesc(itemRequest.getId())
                        .stream()
                        .map((item) -> itemMapper.toRequestedItemRequest(item, itemRequest.getId()))
                        .collect(toList()));
    }
}
