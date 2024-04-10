package ru.practicum.shareit.booking.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingServiceImp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingServiceImp service;
    @InjectMocks
    private BookingController controller;

    @Test
    void getBooking() {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        when(service.getBooking(anyInt(), anyInt())).thenReturn(bookingResponseDto);

        assertEquals(bookingResponseDto, controller.getBooking(1, 1));
    }

    @Test
    void getOwnerBookings() {
        List<BookingResponseDto> bookingResponse = List.of();

        when(service.getBookingListByItemOwner(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(bookingResponse);

        assertEquals(bookingResponse, controller.getBookingListByItemOwner(1, "ALL", 0, 1));
    }

    @Test
    void getUserBookings() {
        List<BookingResponseDto> bookingResponse = List.of();

        when(service.getBookingList(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(bookingResponse);

        assertEquals(bookingResponse, controller.getBookingList(1, "ALL", 0, 1));
    }

    @Test
    void addBooking() {
        Integer userId = 1;
        BookingDto bookingDto = new BookingDto();
        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        BindingResult result = mock(BindingResult.class);

        when(service.addBooking(userId, bookingDto, result)).thenReturn(bookingResponseDto);

        assertEquals(bookingResponseDto, controller.addBooking(userId, bookingDto, result));
    }

    @Test
    void approveBooking() {
        Integer userId = 1;
        Integer bookingId = 1;
        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        when(service.updateStatus(userId, bookingId, "APPROVED")).thenReturn(bookingResponseDto);

        assertEquals(bookingResponseDto, controller.updateStatus(userId, bookingId, "APPROVED"));
    }
}