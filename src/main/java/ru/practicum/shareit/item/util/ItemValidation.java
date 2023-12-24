package ru.practicum.shareit.item.util;

import org.springframework.validation.BindingResult;
import ru.practicum.shareit.error.exception.ValidationException;

public class ItemValidation {
    public static void validation(BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Ошибка валидации");
        }
    }
}
