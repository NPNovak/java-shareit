package ru.practicum.shareit.booking.validation;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidationDateEndAfterStart implements ConstraintValidator<DateEndAfterStart, BookingDto> {

    @Override
    public void initialize(DateEndAfterStart constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookingDto booking, ConstraintValidatorContext context) {
        return booking == null ||
                booking.getEnd() == null ||
                booking.getStart() == null ||
                booking.getEnd().isAfter(booking.getStart());
    }
}
