package ru.practicum.shareit.booking.service;

import org.springframework.validation.BindingResult;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.error.exception.ValidationException;

import java.util.List;


public interface BookingService {
    BookingResponseDto addBooking(Integer userId, BookingDto bookingDto, BindingResult bindingResult) throws ValidationException;

    BookingResponseDto updateStatus(Integer userId, Integer bookingId, String approved) throws ValidationException;

    BookingResponseDto getBooking(Integer userId, Integer bookingId);

    List<BookingResponseDto> getBookingList(Integer userId, String state, int from, int size) throws ValidationException;

    List<BookingResponseDto> getBookingListByItemOwner(Integer userId, String state, int from, int size) throws ValidationException;
}
