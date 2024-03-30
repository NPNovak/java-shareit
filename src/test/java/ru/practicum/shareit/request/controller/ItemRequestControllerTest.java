package ru.practicum.shareit.request.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private ItemRequestServiceImpl service;
    @InjectMocks
    private ItemRequestController controller;

    @Test
    void getItemRequest() {
        Integer userId = 1;
        Integer requestId = 1;

        ItemRequestDto itemRequestDto = new ItemRequestDto();

        when(service.getItemRequest(userId, requestId)).thenReturn(itemRequestDto);

        assertEquals(itemRequestDto, controller.getItemRequest(userId, requestId));
    }

    @Test
    void getOwnerItemRequests() {
        Integer userId = 1;

        List<ItemRequestDto> itemRequestDto = List.of();

        when(service.getOwnerItemRequests(userId)).thenReturn(itemRequestDto);

        assertEquals(itemRequestDto, controller.getOwnerItemRequests(userId));
    }

    @Test
    void getItemRequests() {
        Integer userId = 1;
        int from = 0;
        int size = 1;

        List<ItemRequestDto> itemRequestResponse = List.of();

        when(service.getItemRequests(userId, from, size)).thenReturn(itemRequestResponse);

        assertEquals(itemRequestResponse, controller.getItemRequests(userId, from, size));
    }

    @Test
    void addItemRequest() {
        Integer userId = 1;

        RequestItemRequest requestItemRequest = new RequestItemRequest();
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        when(service.addItemRequest(userId, requestItemRequest)).thenReturn(itemRequestDto);

        assertEquals(itemRequestDto, controller.addItemRequest(userId, requestItemRequest));
    }
}