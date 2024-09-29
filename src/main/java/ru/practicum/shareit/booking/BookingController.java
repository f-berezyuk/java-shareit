package ru.practicum.shareit.booking;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingCreateReq;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.common.ErrorResponse;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UserNotAuthorizedException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addNewBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestBody @Valid BookingCreateReq bookingCreateReq) {
        return bookingService.addNewBooking(userId, bookingCreateReq);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping()
    public List<BookingDto> getAllBooking(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getAllBooking(userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam Boolean approved) {
        return bookingService.updateStatus(userId, bookingId, approved);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAny(final RuntimeException e) {
        return new ErrorResponse(
                "Ошибка: ",
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
}
