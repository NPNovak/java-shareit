package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse valid(MethodArgumentNotValidException exception) {
        List<String> exceptionMessages = new ArrayList<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            exceptionMessages.add(fieldError.getDefaultMessage());
        }

        ErrorResponse response = new ErrorResponse("Ошибка при валидации объекта",
                exceptionMessages.toString());

        log.warn(response.toString());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse validated(ConstraintViolationException exception) {
        ErrorResponse response = new ErrorResponse("Ошибка при валидации параметра",
                exception.getMessage());
        log.warn(response.toString());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse invalidRequest(HttpMessageNotReadableException exception) {
        ErrorResponse response = new ErrorResponse("Запрос составлен неправильно",
                exception.getMessage());
        log.warn(response.toString());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse missingRequestHeaders(MissingRequestHeaderException exception) {
        ErrorResponse response = new ErrorResponse("Отсутствует необзодимое поле Headers",
                exception.getMessage());
        log.warn(response.toString());
        return response;
    }
}
