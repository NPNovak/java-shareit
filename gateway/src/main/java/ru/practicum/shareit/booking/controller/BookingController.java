package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ValidationException;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient client;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                             @PathVariable("bookingId") Integer bookingId) {
        log.debug("Получен GET запрос на получение данных о конкретном бронировании: bookingId - " + bookingId + " userId - " + userId);
        return client.getBooking(userId, bookingId);
    }

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                             @Valid @RequestBody BookingDto bookingDto) throws ValidationException {
        log.debug("Получен POST запрос на добавление нового бронирования: dto - " + bookingDto + " userId - " + userId);
        return client.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                           @PathVariable("bookingId") Integer bookingId,
                                           @RequestParam String approved) throws ValidationException {
        log.debug("Получен PATCH запрос на подтверждение бронирования: bookingId - " + bookingId + " userId - " + userId);
        return client.updateStatus(userId, bookingId, approved);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingList(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(defaultValue = "0") @Min(value = 0, message = "From must be greater than or equal to 0") int from,
                                                   @RequestParam(defaultValue = "20") @Min(value = 0, message = "Size must be greater than or equal to 0") int size) throws ValidationException {

        log.debug("Получен GET запрос на получение данных о бронировании текущего пользователя: state - " + state + " userId - " + userId);
        return client.getBookingList(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingListByItemOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                              @RequestParam(defaultValue = "ALL") String state,
                                                              @RequestParam(defaultValue = "0") @Min(value = 0, message = "From must be greater than or equal to 0") int from,
                                                              @RequestParam(defaultValue = "20") @Min(value = 0, message = "Size must be greater than or equal to 0") int size) throws ValidationException {
        log.debug("Получен GET запрос на получение списка бронирований для всех вещей текущего пользователя: state - " + state + " userId - " + userId);
        return client.getBookingListByItemOwner(userId, state, from, size);
    }
}
