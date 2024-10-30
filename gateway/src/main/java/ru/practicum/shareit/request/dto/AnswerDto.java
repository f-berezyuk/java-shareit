package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
    Long id;
    Long itemId;
    String name;
    Long ownerId;

    @Override
    public String toString() {
        return String.format("AnswerDto{id=%d, itemId=%d, name=%s, ownerId=%d}", id, itemId, name, ownerId);
    }
}
