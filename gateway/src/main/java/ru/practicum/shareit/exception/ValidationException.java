package ru.practicum.shareit.exception;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationException extends RuntimeException {
    public ValidationException(String errorMessage) {
        super(errorMessage);
        log.error(errorMessage);
    }
}