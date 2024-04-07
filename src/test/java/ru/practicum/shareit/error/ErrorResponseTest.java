package ru.practicum.shareit.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorResponseTest {

    @Test
    public void testErrorResponse() {
        String error = "An error occurred.";
        String timestamp = "2023-03-08T15:30:00.000Z";

        ErrorResponse errorResponse = new ErrorResponse(error, timestamp);

        assertEquals(error, errorResponse.getError());
        assertEquals(timestamp, errorResponse.getTimestamp());
    }
}