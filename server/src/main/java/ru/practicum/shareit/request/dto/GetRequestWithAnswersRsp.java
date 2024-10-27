package ru.practicum.shareit.request.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetRequestWithAnswersRsp {
    Long id;
    String description;
    LocalDateTime createdAt;
    List<AnswerDto> items;
}

