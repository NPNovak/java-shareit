package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingResponseDto {

    private int id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Item item;

    private User booker;

    private String status;
}
