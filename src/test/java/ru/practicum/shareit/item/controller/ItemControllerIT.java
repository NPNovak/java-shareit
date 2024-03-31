package ru.practicum.shareit.item.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemServiceImp;

@WebMvcTest(ItemController.class)
class ItemControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemServiceImp itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test item");
        itemDto.setDescription("test");
        itemDto.setAvailable(true);

        ItemResponseDto itemResponseDto = new ItemResponseDto();
        itemResponseDto.setId(0);

        ItemResponseDto responseDto = new ItemResponseDto();

        when(itemService.addItem(anyInt(), any(ItemDto.class), any(BindingResult.class))).thenReturn(responseDto);

        String response = mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        verify(itemService, times(1)).addItem(any(), any(), any());
        assertEquals(response, objectMapper.writeValueAsString(itemResponseDto));
    }

    @Test
    void updateItem() throws Exception {
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("Test item updated");

        ItemResponseDto itemResponseDto = new ItemResponseDto();

        when(itemService.updateItem(any(), any(), any())).thenReturn(new ItemResponseDto());

        String response = mockMvc.perform(patch("/items/1")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemUpdateDto)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        verify(itemService, times(1)).updateItem(any(), any(), any());
        assertEquals(response, objectMapper.writeValueAsString(itemResponseDto));
    }

    @Test
    void addComment() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test comment");

        CommentResponseDto commentResponseDto = new CommentResponseDto();

        when(itemService.addComment(any(), any(), any())).thenReturn(new CommentResponseDto());

        String response = mockMvc.perform(post("/items/1/comment")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        verify(itemService, times(1)).addComment(any(), any(), any());
        assertEquals(response, objectMapper.writeValueAsString(commentResponseDto));
    }

    @Test
    void search() throws Exception {
        when(itemService.search("test", 0, 20)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search?text=test")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(itemService, times(1)).search("test", 0, 20);
    }

    @Test
    void getAllItems() throws Exception {
        when(itemService.getAllItems(1, 0, 20)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

        verify(itemService, times(1)).getAllItems(1, 0, 20);
    }
}