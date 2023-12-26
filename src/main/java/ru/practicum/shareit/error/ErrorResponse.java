package ru.practicum.shareit.error;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String error;
    private String description;
}
