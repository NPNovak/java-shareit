package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.util.BookingValidation;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImp implements BookingService{

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper mapper;


    @Transactional
    public BookingResponseDto addBooking(Integer userId, BookingDto bookingDto, BindingResult bindingResult) throws ValidationException {
        BookingValidation.validation(bindingResult);
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new NotFoundException("Товара с такими id нет"));
        if (!item.getAvailable()) {
            throw new ValidationException("Товар недоступен для бронирования");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователя с такими id нет"));
        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new ValidationException("Время начала не может быть равно времени окончания заявки");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) || bookingDto.getEnd() == bookingDto.getStart()) {
            throw new ValidationException("Ошибка валидации времени");
        }
        if (item.getOwner().getId() == userId) {
            throw new NotFoundException("User с заданным id = " + userId + " является владельцем");
        }

        Booking booking = mapper.toBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus("WAITING");
        bookingRepository.save(booking);
        log.info("Добавлена новая бронь товара");
        return mapper.toBookingResponse(booking);
    }

    @Transactional
    public BookingResponseDto updateStatus(Integer userId, Integer bookingId, String approved) throws ValidationException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Брони с такими id нет"));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundException("Товар не приндалежит данному пользователю");
        }
        if (booking.getStatus().equals("APPROVED")) {
            throw new ValidationException("Бронь товара уже согласована");
        }
        if (approved.equals("true")) {
            booking.setStatus("APPROVED");
        } else {
            booking.setStatus("REJECTED");
        }
        bookingRepository.save(booking);
        log.info("Обновлен статус бронирования");
        return mapper.toBookingResponse(booking);
    }

    public BookingResponseDto getBooking(Integer userId, Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Брони с такими id нет"));
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new NotFoundException("Товар или бронь не приндалежит данному пользователю");
        }
        log.info("Бронирование отображено");
        return mapper.toBookingResponse(booking);
    }

    public List<BookingResponseDto> getBookingList(Integer userId, String state) throws ValidationException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователя с такими id нет"));
        List<Booking> bookingList;
        switch (state) {
            case "ALL":
                bookingList = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                break;
            case "CURRENT":
                bookingList = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case "PAST":
                bookingList = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case "FUTURE":
                bookingList = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case "WAITING":
                bookingList = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, "WAITING");
                break;
            case "REJECTED":
                bookingList = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, "REJECTED");
                break;
            default:
                throw new ValidationException(String.format("Unknown state: %s", state));
        }
        log.info("Список бронирований отображен");
        return bookingList.stream().map(mapper::toBookingResponse).collect(Collectors.toList());
    }

    public List<BookingResponseDto> getBookingListByItemOwner(Integer userId, String state) throws ValidationException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователя с такими id нет"));
        List<Booking> bookingList;
        switch (state) {
            case "ALL":
                bookingList = bookingRepository.findAllByItemOwnerOrderByStartDesc(user);
                break;
            case "CURRENT":
                bookingList = bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(user, LocalDateTime.now(), LocalDateTime.now());
                break;
            case "PAST":
                bookingList = bookingRepository.findAllByItemOwnerAndEndBeforeOrderByStartDesc(user, LocalDateTime.now());
                break;
            case "FUTURE":
                bookingList = bookingRepository.findAllByItemOwnerAndStartAfterOrderByStartDesc(user, LocalDateTime.now());
                break;
            case "WAITING":
                bookingList = bookingRepository.findAllByItemOwnerAndStatusOrderByStartDesc(user, "WAITING");
                break;
            case "REJECTED":
                bookingList = bookingRepository.findAllByItemOwnerAndStatusOrderByStartDesc(user, "REJECTED");
                break;
            default:
                throw new ValidationException(String.format("Unknown state: %s", state));
        }
        log.info("Список бронирований отображен");
        return bookingList.stream().map(mapper::toBookingResponse).collect(Collectors.toList());
    }
}
