package ru.practicum.shareit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UserNotAuthorizedException;
import ru.practicum.shareit.user.exception.UserEmailDuplicateException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

@RestController
@RequestMapping("/test")
public class ErrorHandlingTestController {

    @GetMapping("/user-not-found")
    public void userNotFound() {
        throw new UserNotFoundException("Details about the missing user");
    }

    @GetMapping("/booking-not-found")
    public void bookingNotFound() {
        throw new BookingNotFoundException("Details about the missing booking");
    }

    @GetMapping("/item-not-found")
    public void itemNotFound() {
        throw new ItemNotFoundException("Details about the missing item");
    }

    @GetMapping("/user-not-authorized")
    public void userNotAuthorized() {
        throw new UserNotAuthorizedException("Unauthorized access attempt");
    }

    @GetMapping("/user-email-duplicate")
    public void userEmailDuplicate() {
        throw new UserEmailDuplicateException("Duplicate email found");
    }

    @GetMapping("/illegal-argument")
    public void illegalArgument() {
        throw new IllegalArgumentException("Illegal argument provided");
    }

    @GetMapping("/runtime-exception")
    public void runtimeException() {
        throw new RuntimeException("Runtime exception occurred");
    }
}