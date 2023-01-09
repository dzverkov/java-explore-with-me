package ru.practicum.server.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.server.utils.EwmDateTimeFormatter;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class EwmExceptionHandler {

    private final EwmDateTimeFormatter dateTimeFormatter;


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(final ConstraintViolationException e) {
        log.error("Ошибка параметров: " + e.getMessage());
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Параметры запроса не соответствуют заданным условиям.")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(dateTimeFormatter.toString(LocalDateTime.now()))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(final MethodArgumentNotValidException e) {
        log.error("Ошибка проверки объекта: " + e.getMessage());
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Значение одного из полей объекта не соответствует требованиям.")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(dateTimeFormatter.toString(LocalDateTime.now()))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(final MissingRequestValueException e) {
        log.error("Отсутствует значение параметра: " + e.getMessage());
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Отсутствует значение одного из параметров запроса.")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(dateTimeFormatter.toString(LocalDateTime.now()))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handle(final ValidationException e) {
        log.error("Ошибка проверки объекта: " + e.getMessage());
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Операция не соответствует требованиям.")
                .status(HttpStatus.FORBIDDEN.toString())
                .timestamp(dateTimeFormatter.toString(LocalDateTime.now()))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handle(final NotFoundException e) {
        log.error("Объект не найден: " + e.getMessage());
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Объект не найден.")
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(dateTimeFormatter.toString(LocalDateTime.now()))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(final ConflictException e) {
        log.error("Конфликт данных: " + e.getMessage());
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Конфликт данных.")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(dateTimeFormatter.toString(LocalDateTime.now()))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handle(final Throwable e) {
        log.error("Ошибка сервера: " + e.getMessage());
        return ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getClass().getName() + ": " + e.getMessage())
                .reason("Внутренняя ошибка сервера.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .timestamp(dateTimeFormatter.toString(LocalDateTime.now()))
                .build();
    }


}
