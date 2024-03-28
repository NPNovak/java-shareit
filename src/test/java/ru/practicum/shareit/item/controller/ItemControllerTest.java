package ru.practicum.shareit.item.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemServiceImp;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemServiceImp itemService;

    @Mock
    private BindingResult bindingResult;

    @Test
    void addItem() throws ValidationException {
        var itemDto = new ItemDto();
        itemDto.setName("Test item");
        when(itemService.addItem(any(), any(), any())).thenReturn(new ItemResponseDto());

        var response = itemController.addItem(1, itemDto, bindingResult);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addItemWithException() throws ValidationException {
        var itemDto = new ItemDto();
        itemDto.setName("Test item");

        when(itemService.addItem(any(), any(), any())).thenThrow(new ValidationException("validation error"));
        assertThrows(ValidationException.class, () -> itemController.addItem(1, itemDto, bindingResult));
    }

    @Test
    void updateItem() {
        var itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("Test item updated");
        when(itemService.updateItem(any(), any(), any())).thenReturn(new ItemResponseDto());

        var response = itemController.updateItem(1, itemUpdateDto, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(itemService, times(1)).updateItem(any(), any(), any());
    }

    @Test
    void getItem() {
        when(itemService.getItemById(any(), any())).thenReturn(new ItemResponseDto());

        var response = itemController.getItem(1, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(itemService, times(1)).getItemById(any(), any());
    }

    @Test
    void getAllItems() {
        Integer userId = 1;
        int from = 0;
        int size = 1;

        Collection<ItemResponseDto> itemControllerResponse = List.of();

        when(itemService.getAllItems(1, 0, 1)).thenReturn(itemControllerResponse);

        var response = itemController.getAllItems(userId, from, size);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addComment() {
        Integer userId = 1;
        Integer itemId = 1;
        CommentDto commentDto = new CommentDto();
        CommentResponseDto commentResponseDto = new CommentResponseDto();

        when(itemService.addComment(userId, commentDto, itemId)).thenReturn(commentResponseDto);

        var response = itemController.addComment(userId, commentDto, itemId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void searchItems_whenTextExist() {
        String text = "text";
        int from = 0;
        int size = 1;
        List<ItemResponseDto> itemControllerResponse = List.of();

        when(itemService.search(text, from, size)).thenReturn(itemControllerResponse);

        var response = itemController.search(text, from, size);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}