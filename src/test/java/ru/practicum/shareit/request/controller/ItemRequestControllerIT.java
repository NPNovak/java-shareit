package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestService service;

    @Test
    @SneakyThrows
    void getItemRequest_whenAllOk() {
        Integer userId = 1;
        Integer requestId = 1;

        ItemRequestDto itemRequestDto = new ItemRequestDto();

        when(service.getItemRequest(userId, requestId)).thenReturn(itemRequestDto);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(itemRequestDto));
    }

    @Test
    @SneakyThrows
    void getOwnerItemRequests_whenAllOk() {
        Integer userId = 1;

        List<ItemRequestDto> itemRequestResponse = List.of();

        when(service.getOwnerItemRequests(userId)).thenReturn(itemRequestResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(itemRequestResponse));
    }

    @Test
    @SneakyThrows
    void getItemRequests_whenAllOk() {
        Integer userId = 1;
        int from = 0;
        int size = 1;

        List<ItemRequestDto> itemRequestResponse = List.of();

        when(service.getItemRequests(userId, from, size)).thenReturn(itemRequestResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from + "")
                        .param("size", size + ""))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(itemRequestResponse));
    }

    @Test
    @SneakyThrows
    void addItemRequest_whenAllOk() {
        Integer userId = 1;

        RequestItemRequest requestItemRequest = new RequestItemRequest();
        requestItemRequest.setDescription("description");

        ItemRequestDto itemRequestDto = new ItemRequestDto();

        when(service.addItemRequest(userId, requestItemRequest)).thenReturn(itemRequestDto);

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestItemRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(itemRequestDto));
    }
}