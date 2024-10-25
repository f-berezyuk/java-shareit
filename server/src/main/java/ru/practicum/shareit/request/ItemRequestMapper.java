package ru.practicum.shareit.request;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.Answer;
import ru.practicum.shareit.request.model.ItemRequest;

public class ItemRequestMapper {
    public static ItemRequestDto toDto(ItemRequest request) {
        ItemRequestDto.ItemRequestDtoBuilder builder = ItemRequestDto.builder().id(request.getId());
        if (request.getAnswers() != null) {
            builder.items(request.getAnswers().stream().map(Answer::getItem).map(Item::getId).toList());
        }
        builder.description(request.getDescription())
                .created(request.getCreated().toLocalDateTime());
        return builder
                .build();
    }
}
