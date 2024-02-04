package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.error.exception.ValidationException;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto addBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                         @Valid @RequestBody BookingDto bookingDto, BindingResult bindingResult) throws ValidationException {
        log.info("Получен POST запрос на добавление нового бронирования");
        return bookingService.addBooking(userId, bookingDto, bindingResult);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateStatus(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                        @PathVariable("bookingId") Integer bookingId,
                                        @RequestParam String approved) throws ValidationException {
        log.info("Получен PATCH запрос на подтверждение бронирования");
        return bookingService.updateStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                      @PathVariable("bookingId") Integer bookingId) {
        log.info("Получен GET запрос на получение данных о конкретном бронировании");
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getBookingList(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                @RequestParam(defaultValue = "ALL") String state) throws ValidationException {


        log.info("Получен GET запрос на получение данных о бронировании текущего пользователя");
        return bookingService.getBookingList(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingListByItemOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                           @RequestParam(defaultValue = "ALL") String  state) throws ValidationException {
        log.info("Получен GET запрос на получение списка бронирований для всех вещей текущего пользователя");
        return bookingService.getBookingListByItemOwner(userId, state);
    }
}
