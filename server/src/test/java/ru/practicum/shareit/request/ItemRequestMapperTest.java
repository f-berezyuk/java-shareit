package ru.practicum.shareit.request;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.AnswerDto;
import ru.practicum.shareit.request.dto.GetRequestWithAnswersRsp;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.Answer;
import ru.practicum.shareit.request.model.ItemRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRequestMapperTest {

    @Test
    void testToDto() {
        LocalDateTime created = LocalDateTime.of(2023, 10, 1, 12, 0);
        Item item = new Item(1L, 1L, "Item Name", "Item Description", true, null, null);
        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("Description")
                .created(Timestamp.valueOf(created))
                .build();
        Answer answer = new Answer(1L, item, request);
        request.setAnswers(List.of(answer));

        ItemRequestDto result = ItemRequestMapper.toDto(request);

        assertThat(result.getId()).isEqualTo(request.getId());
        assertThat(result.getDescription()).isEqualTo(request.getDescription());
        assertThat(result.getCreated()).isEqualTo(created);
        assertThat(result.getItems()).hasSize(1);

        AnswerDto answerDto = result.getItems().get(0);
        assertThat(answerDto.getId()).isEqualTo(answer.getId());
        assertThat(answerDto.getItemId()).isEqualTo(answer.getItem().getId());
        assertThat(answerDto.getName()).isEqualTo(answer.getItem().getName());
        assertThat(answerDto.getOwnerId()).isEqualTo(answer.getItem().getOwnerId());
    }

    @Test
    void testToResponse() {
        LocalDateTime created = LocalDateTime.of(2023, 10, 1, 12, 0);
        Item item = new Item(1L, 1L, "Item Name", "Item Description", true, null, null);
        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("Description")
                .created(Timestamp.valueOf(created))
                .build();
        Answer answer = new Answer(1L, item, request);
        request.setAnswers(List.of(answer));

        GetRequestWithAnswersRsp result = ItemRequestMapper.toResponse(request);

        assertThat(result.getId()).isEqualTo(request.getId());
        assertThat(result.getDescription()).isEqualTo(request.getDescription());
        assertThat(result.getCreatedAt()).isEqualTo(created);
        assertThat(result.getItems()).hasSize(1);

        AnswerDto answerDto = result.getItems().get(0);
        assertThat(answerDto.getId()).isEqualTo(answer.getId());
        assertThat(answerDto.getItemId()).isEqualTo(answer.getItem().getId());
        assertThat(answerDto.getName()).isEqualTo(answer.getItem().getName());
        assertThat(answerDto.getOwnerId()).isEqualTo(answer.getItem().getOwnerId());
    }

    // Optionally, add tests for cases where `answers` are null or empty
    @Test
    void testToDtoWithNoAnswers() {
        LocalDateTime created = LocalDateTime.of(2023, 10, 1, 12, 0);
        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("Description")
                .created(Timestamp.valueOf(created))
                .build();

        ItemRequestDto result = ItemRequestMapper.toDto(request);

        assertThat(result.getItems()).isNullOrEmpty();
    }

    @Test
    void testToResponseWithNoAnswers() {
        LocalDateTime created = LocalDateTime.of(2023, 10, 1, 12, 0);
        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("Description")
                .created(Timestamp.valueOf(created))
                .build();

        GetRequestWithAnswersRsp result = ItemRequestMapper.toResponse(request);

        assertThat(result.getItems()).isNullOrEmpty();
    }
}