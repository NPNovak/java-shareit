package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {

    private int id;

    private String email;

    private String name;
}
