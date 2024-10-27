package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.EBookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    Long id;
    @JsonFormat(pattern = "YYYY-MM-dd'T'HH:mm:ss")
    LocalDateTime start;
    @JsonFormat(pattern = "YYYY-MM-dd'T'HH:mm:ss")
    LocalDateTime end;
    EBookingStatus status;
    UserDto booker;
    ItemDto item;
}
