package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookingMapperImpl.class)
public class BookingMapperTest {

    @InjectMocks
    private BookingMapperImpl bookingMapper;

    @Test
    public void testToBookingDto() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));

        BookingDto bookingDto = bookingMapper.toBookingDto(booking);

        BookingDto expectedBookingDto = new BookingDto();
        expectedBookingDto.setId(1);
        expectedBookingDto.setStart(LocalDateTime.now());
        expectedBookingDto.setEnd(LocalDateTime.now().plusDays(1));

        assertEquals(expectedBookingDto, bookingDto);
    }

    @Test
    public void testToBookingDtoNull() {
        assertNull(bookingMapper.toBookingDto((Booking) null));
    }

    @Test
    public void testToBooking() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));

        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());

        assertEquals(booking, bookingMapper.toBooking(bookingDto));
    }

    @Test
    public void testToBookingNull() {
        assertNull(bookingMapper.toBooking((BookingDto) null));
    }

    @Test
    public void testToBookingResponse() {
        Item item = new Item();
        User user = new User();

        Booking booking = new Booking();
        booking.setId(1);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));

        BookingResponseDto bookingResponseDto = bookingMapper.toBookingResponse(booking);

        BookingResponseDto expectedBookingResponseDto = new BookingResponseDto();
        expectedBookingResponseDto.setId(1);
        expectedBookingResponseDto.setItem(item);
        expectedBookingResponseDto.setBooker(user);
        expectedBookingResponseDto.setStart(booking.getStart());
        expectedBookingResponseDto.setEnd(booking.getEnd());

        assertEquals(expectedBookingResponseDto, bookingResponseDto);
    }

    @Test
    public void testToBookingResponseNull() {
        assertNull(bookingMapper.toBookingResponse((Booking) null));
    }

    @Test
    public void testToBookingFromBookingResponseDto() {
        Item item = new Item();
        User user = new User();

        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(1);
        bookingResponseDto.setItem(item);
        bookingResponseDto.setBooker(user);
        bookingResponseDto.setStart(LocalDateTime.now());
        bookingResponseDto.setEnd(LocalDateTime.now().plusDays(1));

        Booking booking = bookingMapper.toBooking(bookingResponseDto);

        Booking expectedBooking = new Booking();
        expectedBooking.setId(1);
        expectedBooking.setItem(item);
        expectedBooking.setBooker(user);
        expectedBooking.setStart(bookingResponseDto.getStart());
        expectedBooking.setEnd(bookingResponseDto.getEnd());

        assertEquals(expectedBooking, booking);
    }

    @Test
    public void testToBookingFromResponseNull() {
        assertNull(bookingMapper.toBooking((BookingResponseDto) null));
    }
}