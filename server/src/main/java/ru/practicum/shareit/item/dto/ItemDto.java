package ru.practicum.shareit.item.dto;

import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;

@Data
@NoArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    @Nullable
    private List<CommentDto> comments;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
}
