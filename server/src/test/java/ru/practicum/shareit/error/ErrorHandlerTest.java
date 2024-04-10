package ru.practicum.shareit.error;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.error.exception.NotFoundException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {
    @Test
    void handleNotFoundException_ReturnsMapWithNotFoundMessage() {
        ErrorHandler errorHandler = new ErrorHandler();
        NotFoundException exception = new NotFoundException("Resource not found");
        Map<String, String> result = errorHandler.handleNotFoundException(exception);
        assertEquals(Map.of("NOT_FOUND", "Resource not found"), result);
    }

    @Test
    void handleInternalException_ReturnsMapWithInternalErrorMessage() {
        ErrorHandler errorHandler = new ErrorHandler();
        Exception exception = new Exception("Internal server error");
        Map<String, String> result = errorHandler.handleInternalException(exception);
        assertEquals(Map.of("INTERNAL_SERVER_ERROR", "Internal server error"), result);
    }
}