package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingServiceImp;
import ru.practicum.shareit.error.exception.ValidationException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingServiceImp bookingService;

    @PostMapping
    public BookingResponseDto addBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                         @Valid @RequestBody BookingDto bookingDto, BindingResult bindingResult) throws ValidationException {
        log.debug("Получен POST запрос на добавление нового бронирования: dto - " + bookingDto + " userId - " + userId);
        return bookingService.addBooking(userId, bookingDto, bindingResult);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateStatus(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                           @PathVariable("bookingId") Integer bookingId,
                                           @RequestParam String approved) throws ValidationException {
        log.debug("Получен PATCH запрос на подтверждение бронирования: bookingId - " + bookingId + " userId - " + userId);
        return bookingService.updateStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                         @PathVariable("bookingId") Integer bookingId) {
        log.debug("Получен GET запрос на получение данных о конкретном бронировании: bookingId - " + bookingId + " userId - " + userId);
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getBookingList(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(defaultValue = "0") @Min(value = 0, message = "From must be greater than or equal to 0") int from,
                                                   @RequestParam(defaultValue = "20") @Min(value = 0, message = "Size must be greater than or equal to 0") int size) throws ValidationException {

        log.debug("Получен GET запрос на получение данных о бронировании текущего пользователя: state - " + state + " userId - " + userId);
        return bookingService.getBookingList(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingListByItemOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                              @RequestParam(defaultValue = "ALL") String state,
                                                              @RequestParam(defaultValue = "0") @Min(value = 0, message = "From must be greater than or equal to 0") int from,
                                                              @RequestParam(defaultValue = "20") @Min(value = 0, message = "Size must be greater than or equal to 0") int size) throws ValidationException {
        log.debug("Получен GET запрос на получение списка бронирований для всех вещей текущего пользователя: state - " + state + " userId - " + userId);
        return bookingService.getBookingListByItemOwner(userId, state, from, size);
    }
}
