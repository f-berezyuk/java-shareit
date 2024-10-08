package ru.practicum.shareit.booking;

import java.util.List;

import ru.practicum.shareit.booking.dto.BookingCreateReq;
import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {
    BookingDto addNewBooking(Long userId, BookingCreateReq bookingCreateReq);

    BookingDto updateStatus(Long userId, Long bookingId, Boolean approved);

    BookingDto getBooking(Long userId, Long bookingId);

    List<BookingDto> getAllBooking(Long userId);
}
