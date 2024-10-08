package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

public class BookingMapper {
    public static BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .booker(UserMapper.toDto(booking.getBooker()))
                .status(booking.getStatus())
                .start(booking.getStartAt().toLocalDateTime())
                .end(booking.getEndAt().toLocalDateTime())
                .item(ItemMapper.toDto(booking.getItem()))
                .build();
    }
}
