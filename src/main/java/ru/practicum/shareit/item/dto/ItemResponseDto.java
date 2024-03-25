package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.user.dto.UserCreateDto;

import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class ItemResponseDto {

    private int id;

    private String name;

    private String description;

    private Boolean available;

    private UserCreateDto owner;

    private ItemRequest request;

    private ItemBookingDto lastBooking;

    private ItemBookingDto nextBooking;

    private List<CommentResponseDto> comments;

    private Integer requestId;
}
