package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingServiceImp;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingServiceImp service;

    @Test
    @SneakyThrows
    void getBooking_whenAllOk() {
        Integer userId = 1;
        Integer bookingId = 1;

        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        when(service.getBooking(userId, bookingId)).thenReturn(bookingResponseDto);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(bookingResponseDto));
    }

    @Test
    @SneakyThrows
    void getOwnerBookings_whenAllOk() {
        Integer userId = 1;
        int from = 0;
        int size = 1;

        List<BookingResponseDto> bookingResponses = List.of();

        when(service.getBookingListByItemOwner(userId, "ALL", from, size)).thenReturn(bookingResponses);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from + "")
                        .param("size", size + ""))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(bookingResponses));
    }

    @Test
    @SneakyThrows
    void getUserBookings_whenAllOk() {
        Integer userId = 1;
        int from = 0;
        int size = 1;

        List<BookingResponseDto> bookingResponses = List.of();

        when(service.getBookingList(userId, "ALL", from, size)).thenReturn(bookingResponses);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", from + "")
                        .param("size", size + ""))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(bookingResponses));
    }

    @Test
    @SneakyThrows
    void addBooking_whenAllOk() {
        Integer userId = 1;
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.of(3000, 1, 1, 1, 1, 1));
        bookingDto.setEnd(LocalDateTime.of(3001, 1, 1, 1, 1, 1));
        bookingDto.setItemId(1);

        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        when(service.addBooking(anyInt(), any(BookingDto.class), any(BindingResult.class))).thenReturn(bookingResponseDto);

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(bookingResponseDto));
    }

    @Test
    @SneakyThrows
    void approveBooking_whenAllOk() {
        Integer userId = 1;
        Integer bookingId = 1;

        BookingResponseDto bookingResponse = new BookingResponseDto();

        when(service.updateStatus(userId, bookingId, "APPROVED")).thenReturn(bookingResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "APPROVED"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(bookingResponse));
    }
}