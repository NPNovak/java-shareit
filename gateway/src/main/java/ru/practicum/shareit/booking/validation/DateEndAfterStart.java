package ru.practicum.shareit.booking.validation;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidationDateEndAfterStart.class)
public @interface DateEndAfterStart {
    String message() default "";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

    String value() default "";
}
