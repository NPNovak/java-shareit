package ru.practicum.shareit.error.exception;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationException extends Exception {
    public ValidationException(String errorMessage) {
        super(errorMessage);
        log.debug("Получен статус 400 Bad Request {}", errorMessage);
    }
}