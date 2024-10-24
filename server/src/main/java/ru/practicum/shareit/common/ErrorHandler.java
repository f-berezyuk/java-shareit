package ru.practicum.shareit.common;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UserNotAuthorizedException;
import ru.practicum.shareit.user.exception.UserEmailDuplicateException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(final ValidationException e) {
        return new ErrorResponse(
                "Ошибка валидации: ",
                e.getMessage()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntimeException(final RuntimeException e) {
        return new ErrorResponse(
                "Ошибка сервера: ",
                e.getMessage()
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(final UserNotFoundException e) {
        return new ErrorResponse("Пользователя не существует.", e.getMessage());
    }

    @ExceptionHandler(BookingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookingNotFound(final BookingNotFoundException e) {
        return new ErrorResponse("Бронирования не существует.", e.getMessage());
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemNotFound(final ItemNotFoundException e) {
        return new ErrorResponse("Предмет не существует.", e.getMessage());
    }

    @ExceptionHandler(UserNotAuthorizedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserNotAuth(final UserNotAuthorizedException e) {
        return new ErrorResponse("Доступ запрещён.", e.getMessage());
    }

    @ExceptionHandler(UserEmailDuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(final UserEmailDuplicateException e) {
        return new ErrorResponse(
                "Ошибка регистрации: ",
                e.getMessage()
        );
    }
}
