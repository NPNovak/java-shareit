package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemRequest;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository repository;
    @Mock
    private ItemRequestMapper mapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;
    @InjectMocks
    private ItemRequestServiceImpl service;

    @Test
    void getItemRequest_whenAllOk() {
        User user = new User();
        ItemRequest itemRequest = new ItemRequest();
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(repository.findById(anyInt())).thenReturn(Optional.of(itemRequest));
        when(mapper.toItemRequestDto(itemRequest, List.of())).thenReturn(itemRequestDto);

        assertEquals(itemRequestDto, service.getItemRequest(1, 1));
    }

    @Test
    void getItemRequest_whenUserNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getItemRequest(1, 1));
    }

    @Test
    void getItemRequest_whenItemRequestNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getItemRequest(1, 1));
    }

    @Test
    void getOwnerItemRequests_whenAllOk() {
        User user = new User();
        ItemRequest itemRequest = new ItemRequest();
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(repository.findByRequesterIdOrderByCreatedDesc(anyInt())).thenReturn(List.of(itemRequest, itemRequest, itemRequest));
        when(mapper.toItemRequestDto(itemRequest, List.of())).thenReturn(itemRequestDto);

        List<ItemRequestDto> itemsRequestResponse = service.getOwnerItemRequests(1);

        assertEquals(3, itemsRequestResponse.size());
        assertEquals(itemRequestDto, itemsRequestResponse.get(0));
        assertEquals(itemRequestDto, itemsRequestResponse.get(1));
        assertEquals(itemRequestDto, itemsRequestResponse.get(2));
    }

    @Test
    void getOwnerItemRequests_whenUserNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getItemRequest(1, 1));
    }

    @Test
    void getItemRequests_whenAllOk() {
        User user = new User();
        ItemRequest itemRequest = new ItemRequest();
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(repository.findByRequesterIdNot(anyInt(), any(Pageable.class))).thenReturn(List.of(itemRequest, itemRequest, itemRequest));
        when(mapper.toItemRequestDto(itemRequest, List.of())).thenReturn(itemRequestDto);

        List<ItemRequestDto> itemsRequestResponse = service.getItemRequests(1, 1, 1);

        assertEquals(3, itemsRequestResponse.size());
        assertEquals(itemRequestDto, itemsRequestResponse.get(0));
        assertEquals(itemRequestDto, itemsRequestResponse.get(1));
        assertEquals(itemRequestDto, itemsRequestResponse.get(2));
    }

    @Test
    void getItemRequests_whenUserNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getItemRequests(1, 1, 1));
    }

    @Test
    void addItemRequest_whenAllOk() {
        User user = new User();
        RequestItemRequest requestItemRequest = new RequestItemRequest();
        requestItemRequest.setDescription("test");
        ItemRequest itemRequest = new ItemRequest();
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(mapper.toItemRequest(requestItemRequest)).thenReturn(itemRequest);
        when(repository.save(itemRequest)).thenReturn(itemRequest);
        when(mapper.toItemRequestDto(itemRequest)).thenReturn(itemRequestDto);

        assertEquals(itemRequestDto, service.addItemRequest(1, requestItemRequest));
    }

    @Test
    void addItemRequest_whenUserNotFound() {
        assertThrows(ValidationException.class, () -> service.addItemRequest(1, new RequestItemRequest()));
    }
}