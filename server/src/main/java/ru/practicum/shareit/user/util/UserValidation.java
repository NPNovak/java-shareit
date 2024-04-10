package ru.practicum.shareit.user.util;

import org.springframework.validation.BindingResult;
import ru.practicum.shareit.error.exception.ValidationException;

public class UserValidation {
    public static void validation(BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Ошибка валидации");
        }
    }
}
