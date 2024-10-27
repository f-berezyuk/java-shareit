package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.AnswerDto;
import ru.practicum.shareit.request.dto.GetRequestWithAnswersRsp;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

public class ItemRequestMapper {
    public static ItemRequestDto toDto(ItemRequest request) {
        ItemRequestDto.ItemRequestDtoBuilder builder = ItemRequestDto.builder().id(request.getId());
        if (request.getAnswers() != null) {
            builder.items(request.getAnswers().stream()
                    .map(answer -> AnswerDto.builder()
                            .id(answer.getId())
                            .itemId(answer.getItem().getId())
                            .name(answer.getItem().getName())
                            .ownerId(answer.getItem().getOwnerId())
                            .build())
                    .toList());
        }
        builder.description(request.getDescription())
                .created(request.getCreated().toLocalDateTime());
        return builder
                .build();
    }

    public static GetRequestWithAnswersRsp toResponse(ItemRequest request) {
        GetRequestWithAnswersRsp.GetRequestWithAnswersRspBuilder getRequestWithAnswersRspBuilder =
                GetRequestWithAnswersRsp.builder()
                        .id(request.getId())
                        .description(request.getDescription())
                        .createdAt(request.getCreated().toLocalDateTime());

        if (request.getAnswers() != null) {
            getRequestWithAnswersRspBuilder.items(request.getAnswers().stream()
                    .map(answer -> AnswerDto.builder()
                            .id(answer.getId())
                            .itemId(answer.getItem().getId())
                            .name(answer.getItem().getName())
                            .ownerId(answer.getItem().getOwnerId())
                            .build())
                    .toList());
        }

        return getRequestWithAnswersRspBuilder.build();
    }
}
