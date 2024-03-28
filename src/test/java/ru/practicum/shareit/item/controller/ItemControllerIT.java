package ru.practicum.shareit.item.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
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
        var itemDto = new ItemDto();
        itemDto.setName("Test item");
        when(itemService.addItem(any(), any(), any())).thenReturn(new ItemResponseDto());

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());

        verify(itemService, times(1)).addItem(any(), any(), any());
    }

    @Test
    void addItemWithException() throws Exception {
        var itemDto = new ItemDto();
        itemDto.setName("Test item");
        when(itemService.addItem(any(), any(), any())).thenThrow(new ValidationException("validation error"));

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());

        verify(itemService, times(1)).addItem(any(), any(), any());
    }

    @Test
    void updateItem() throws Exception {
        var itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("Test item updated");
        when(itemService.updateItem(any(), any(), any())).thenReturn(new ItemResponseDto());

        mockMvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemUpdateDto)))
                .andExpect(status().isOk());

        verify(itemService, times(1)).updateItem(any(), any(), any());
    }

    @Test
    void updateItemNotFound() throws Exception {
        var itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("Test item updated");
        when(itemService.updateItem(any(), any(), any())).thenThrow(new NotFoundException("Item not found"));

        mockMvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemUpdateDto)))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).updateItem(any(), any(), any());
    }

    @Test
    void getItemNotFound() throws Exception {
        when(itemService.getItemById(any(), any())).thenThrow(new NotFoundException("Item not found"));

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).getItemById(any(), any());
    }

    @Test
    void addComment() throws Exception {
        var commentDto = new CommentDto();
        commentDto.setText("Test comment");
        when(itemService.addComment(any(), any(), any())).thenReturn(new CommentResponseDto());

        mockMvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk());

        verify(itemService, times(1)).addComment(any(), any(), any());
    }

    @Test
    void addCommentWithValidationException() throws Exception {
        var commentDto = new CommentDto();
        commentDto.setText("");
        when(itemService.addComment(any(), any(), any())).thenThrow(new ValidationException("validation error"));

        mockMvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isBadRequest());

        verify(itemService, times(1)).addComment(any(), any(), any());
    }

    @Test
    void addCommentWithNotFoundException() throws Exception {
        var commentDto = new CommentDto();
        commentDto.setText("Test comment");
        when(itemService.addComment(any(), any(), any())).thenThrow(new NotFoundException("Item not found"));

        mockMvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).addComment(any(), any(), any());
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