package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImpTest {

    @Mock
    private BookingRepository repository;
    @Mock
    private BookingMapper mapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private BookingServiceImp service;

    @Test
    void getBooking_whenAllOk() {
        Booking booking = new Booking();
        booking.setBooker(new User(1, null, null));
        booking.setItem(new Item(1, null, null, null, new User(2, null, null), null));

        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setBooker(booking.getBooker());
        bookingResponseDto.setItem(booking.getItem());

        when(repository.findById(anyInt())).thenReturn(Optional.of(booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponseDto);

        assertEquals(bookingResponseDto, service.getBooking(2, 1));
    }

    @Test
    void getBooking_whenBookingNotFound() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getBooking(1, 1));
    }

    @Test
    void getOwnerBookings_whenAllState() {
        Booking booking = new Booking();
        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(repository.findAllByItemOwner((any(User.class)), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponseDto);

        List<BookingResponseDto> bookingResponses = service.getBookingListByItemOwner(1, "ALL", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponseDto, bookingResponses.get(0));
        assertEquals(bookingResponseDto, bookingResponses.get(1));
        assertEquals(bookingResponseDto, bookingResponses.get(2));
    }

    @Test
    void getOwnerBookings_whenWaitingState() {
        Booking booking = new Booking();
        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(repository.findAllByItemOwnerAndStatus((any(User.class)), anyString(), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponseDto);

        List<BookingResponseDto> bookingResponses = service.getBookingListByItemOwner(1, "WAITING", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponseDto, bookingResponses.get(0));
        assertEquals(bookingResponseDto, bookingResponses.get(1));
        assertEquals(bookingResponseDto, bookingResponses.get(2));
    }

    @Test
    void getOwnerBookings_whenRejectedState() {
        Booking booking = new Booking();
        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(repository.findAllByItemOwnerAndStatus((any(User.class)), anyString(), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponseDto);

        List<BookingResponseDto> bookingResponses = service.getBookingListByItemOwner(1, "REJECTED", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponseDto, bookingResponses.get(0));
        assertEquals(bookingResponseDto, bookingResponses.get(1));
        assertEquals(bookingResponseDto, bookingResponses.get(2));
    }

    @Test
    void getOwnerBookings_whenPastState() {
        Booking booking = new Booking();
        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(repository.findAllByItemOwnerAndEndBefore((any(User.class)), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponseDto);

        List<BookingResponseDto> bookingResponses = service.getBookingListByItemOwner(1, "PAST", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponseDto, bookingResponses.get(0));
        assertEquals(bookingResponseDto, bookingResponses.get(1));
        assertEquals(bookingResponseDto, bookingResponses.get(2));
    }

    @Test
    void getOwnerBookings_whenCurrentState() {
        Booking booking = new Booking();
        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(repository.findAllByItemOwnerAndStartBeforeAndEndAfter((any(User.class)), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponseDto);

        List<BookingResponseDto> bookingResponses = service.getBookingListByItemOwner(1, "CURRENT", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponseDto, bookingResponses.get(0));
        assertEquals(bookingResponseDto, bookingResponses.get(1));
        assertEquals(bookingResponseDto, bookingResponses.get(2));
    }

    @Test
    void getOwnerBookings_whenFutureState() {
        Booking booking = new Booking();
        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(repository.findAllByItemOwnerAndStartAfter((any(User.class)), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponseDto);

        List<BookingResponseDto> bookingResponses = service.getBookingListByItemOwner(1, "FUTURE", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponseDto, bookingResponses.get(0));
        assertEquals(bookingResponseDto, bookingResponses.get(1));
        assertEquals(bookingResponseDto, bookingResponses.get(2));
    }

    @Test
    void getUserBookings_whenAllState() {
        Booking booking = new Booking();
        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(repository.findAllByBookerId(anyInt(), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponseDto);

        List<BookingResponseDto> bookingResponses = service.getBookingList(1, "ALL", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponseDto, bookingResponses.get(0));
        assertEquals(bookingResponseDto, bookingResponses.get(1));
        assertEquals(bookingResponseDto, bookingResponses.get(2));
    }

    @Test
    void getUserBookings_whenWaitingState() {
        Booking booking = new Booking();
        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(repository.findAllByBookerIdAndStatus(anyInt(), anyString(), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponseDto);

        List<BookingResponseDto> bookingResponses = service.getBookingList(1, "WAITING", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponseDto, bookingResponses.get(0));
        assertEquals(bookingResponseDto, bookingResponses.get(1));
        assertEquals(bookingResponseDto, bookingResponses.get(2));
    }

    @Test
    void getUserBookings_whenRejectedState() {
        Booking booking = new Booking();
        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(repository.findAllByBookerIdAndStatus(anyInt(), anyString(), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponseDto);

        List<BookingResponseDto> bookingResponses = service.getBookingList(1, "REJECTED", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponseDto, bookingResponses.get(0));
        assertEquals(bookingResponseDto, bookingResponses.get(1));
        assertEquals(bookingResponseDto, bookingResponses.get(2));
    }

    @Test
    void getUserBookings_whenPastState() {
        Booking booking = new Booking();
        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(repository.findAllByBookerIdAndEndBefore(anyInt(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponseDto);

        List<BookingResponseDto> bookingResponses = service.getBookingList(1, "PAST", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponseDto, bookingResponses.get(0));
        assertEquals(bookingResponseDto, bookingResponses.get(1));
        assertEquals(bookingResponseDto, bookingResponses.get(2));
    }

    @Test
    void getUserBookings_whenCurrentState() {
        Booking booking = new Booking();
        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(repository.findAllByBookerIdAndStartBeforeAndEndAfter(anyInt(), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponseDto);

        List<BookingResponseDto> bookingResponses = service.getBookingList(1, "CURRENT", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponseDto, bookingResponses.get(0));
        assertEquals(bookingResponseDto, bookingResponses.get(1));
        assertEquals(bookingResponseDto, bookingResponses.get(2));
    }

    @Test
    void getUserBookings_whenFutureState() {
        Booking booking = new Booking();
        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(repository.findAllByBookerIdAndStartAfter(anyInt(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(booking, booking, booking));
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponseDto);

        List<BookingResponseDto> bookingResponses = service.getBookingList(1, "FUTURE", 1, 1);

        assertEquals(3, bookingResponses.size());
        assertEquals(bookingResponseDto, bookingResponses.get(0));
        assertEquals(bookingResponseDto, bookingResponses.get(1));
        assertEquals(bookingResponseDto, bookingResponses.get(2));
    }

    @Test
    void addBooking_whenAllOk() {
        User booker = new User();
        booker.setId(1);

        User owner = new User();
        owner.setId(2);

        Item item = new Item();
        item.setOwner(owner);
        item.setAvailable(true);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1);
        bookingDto.setStart(LocalDateTime.of(3000, 1, 1, 1, 1, 1));
        bookingDto.setEnd(LocalDateTime.of(3001, 1, 1, 1, 1, 1));

        Booking booking = new Booking();

        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setItem(item);
        bookingResponseDto.setBooker(booker);
        bookingResponseDto.setStatus("WAITING");

        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(booker));
        when(mapper.toBooking(bookingDto)).thenReturn(booking);
        when(repository.save(booking)).thenReturn(booking);
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponseDto);

        BindingResult result = mock(BindingResult.class);

        assertEquals(bookingResponseDto, service.addBooking(1, bookingDto, result));
    }

    @Test
    void approveBooking_whenBookingIsApproved() {
        Booking booking = new Booking();

        User owner = new User();
        owner.setId(1);

        Item item = new Item();
        item.setOwner(owner);

        booking.setItem(item);
        booking.setStatus("WAITING");

        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setItem(item);
        bookingResponseDto.setStatus("APPROVED");

        when(repository.findById(anyInt())).thenReturn(Optional.of(booking));
        when(repository.save(booking)).thenReturn(booking);
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponseDto);

        assertEquals(bookingResponseDto, service.updateStatus(2, 1, "APPROVED"));
    }

    @Test
    void approveBooking_whenBookingIsNotApproved() {
        Booking booking = new Booking();

        User owner = new User();
        owner.setId(1);

        Item item = new Item();
        item.setOwner(owner);

        booking.setItem(item);
        booking.setStatus("WAITING");

        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setItem(item);
        bookingResponseDto.setStatus("REJECTED");

        when(repository.findById(anyInt())).thenReturn(Optional.of(booking));
        when(repository.save(booking)).thenReturn(booking);
        when(mapper.toBookingResponse(booking)).thenReturn(bookingResponseDto);

        assertEquals(bookingResponseDto, service.updateStatus(2, 1, "false"));
    }

    @Test
    void approveBooking_whenItemNotFound() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.updateStatus(1, 1, "APPROVED"));
    }
}