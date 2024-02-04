package ru.practicum.shareit.booking.util;

import org.springframework.validation.BindingResult;
import ru.practicum.shareit.error.exception.ValidationException;

public class BookingValidation {
    public static void validation(BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Ошибка валидации");
        }
    }
}