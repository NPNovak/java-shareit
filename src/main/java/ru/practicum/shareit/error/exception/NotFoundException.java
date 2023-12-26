package ru.practicum.shareit.error.exception;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundException extends RuntimeException {
    public NotFoundException(String errorMessage) {
        super(errorMessage);
        log.debug("Получен статус 404 Not found {}", errorMessage);
    }
}