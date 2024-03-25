package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class BookingServiceImp implements BookingService {

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
        if (item.getOwner().getId().equals(userId)) {
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
        if (booking.getItem().getOwner().getId().equals(userId)) {
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
        if (!booking.getItem().getOwner().getId().equals(userId) && booking.getBooker().getId().equals(userId)) {
            throw new NotFoundException("Товар или бронь не приндалежит данному пользователю");
        }
        log.info("Бронирование отображено");
        return mapper.toBookingResponse(booking);
    }

    public List<BookingResponseDto> getBookingList(Integer userId, String state, int from, int size) throws ValidationException {
        if (from < 0 || size < 0) {
            throw new ValidationException("From and size must be greater than or equal to 0");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователя с такими id нет"));
        List<Booking> bookingList;
        Pageable sortedByStart = PageRequest.of(from / size, size, Sort.by("start").descending());
        switch (state) {
            case "ALL":
                bookingList = bookingRepository.findAllByBookerId(userId, sortedByStart);
                break;
            case "CURRENT":
                bookingList = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(), LocalDateTime.now(), sortedByStart);
                break;
            case "PAST":
                bookingList = bookingRepository.findAllByBookerIdAndEndBefore(userId, LocalDateTime.now(), sortedByStart);
                break;
            case "FUTURE":
                bookingList = bookingRepository.findAllByBookerIdAndStartAfter(userId, LocalDateTime.now(), sortedByStart);
                break;
            case "WAITING":
                bookingList = bookingRepository.findAllByBookerIdAndStatus(userId, "WAITING", sortedByStart);
                break;
            case "REJECTED":
                bookingList = bookingRepository.findAllByBookerIdAndStatus(userId, "REJECTED", sortedByStart);
                break;
            default:
                throw new ValidationException(String.format("Unknown state: %s", state));
        }
        log.info("Список бронирований отображен");
        return bookingList.stream().map(mapper::toBookingResponse).collect(Collectors.toList());
    }

    public List<BookingResponseDto> getBookingListByItemOwner(Integer userId, String state, int from, int size) throws ValidationException {
        if (from < 0 || size < 0) {
            throw new ValidationException("From and size must be greater than or equal to 0");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователя с такими id нет"));
        List<Booking> bookingList;
        Pageable sortedByStart = PageRequest.of(from / size, size, Sort.by("start").descending());
        switch (state) {
            case "ALL":
                bookingList = bookingRepository.findAllByItemOwner(user, sortedByStart);
                break;
            case "CURRENT":
                bookingList = bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfter(user, LocalDateTime.now(), LocalDateTime.now(), sortedByStart);
                break;
            case "PAST":
                bookingList = bookingRepository.findAllByItemOwnerAndEndBefore(user, LocalDateTime.now(), sortedByStart);
                break;
            case "FUTURE":
                bookingList = bookingRepository.findAllByItemOwnerAndStartAfter(user, LocalDateTime.now(), sortedByStart);
                break;
            case "WAITING":
                bookingList = bookingRepository.findAllByItemOwnerAndStatus(user, "WAITING", sortedByStart);
                break;
            case "REJECTED":
                bookingList = bookingRepository.findAllByItemOwnerAndStatus(user, "REJECTED", sortedByStart);
                break;
            default:
                throw new ValidationException(String.format("Unknown state: %s", state));
        }
        log.info("Список бронирований отображен");
        return bookingList.stream().map(mapper::toBookingResponse).collect(Collectors.toList());
    }
}
