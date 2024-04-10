package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.validation.DateEndAfterStart;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@DateEndAfterStart(message = "Дата старта и конца указана неверно")
public class BookingDto {

    private int id;

    @NotNull(message = "поле start не должно быть пустым")
    @FutureOrPresent(message = "поле start должно быть сейчас или в будущем")
    private LocalDateTime start;

    @NotNull(message = "поле end не должно быть пустым")
    @Future(message = "поле end должно быть в будущем")
    private LocalDateTime end;

    @NotNull
    @Min(value = 1, message = "ItemId должно быть больше 0")
    private int itemId;
}