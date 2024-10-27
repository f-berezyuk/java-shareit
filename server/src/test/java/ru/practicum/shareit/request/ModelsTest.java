package ru.practicum.shareit.request;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.AnswerDto;
import ru.practicum.shareit.request.dto.GetRequestWithAnswersRsp;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelsTest {

    @Nested
    class AnswerDtoTest {

        @Test
        void testNoArgsConstructor() {
            AnswerDto answerDto = new AnswerDto();
            assertThat(answerDto).isNotNull();
        }

        @Test
        void testAllArgsConstructor() {
            AnswerDto answerDto = AnswerDto.builder()
                    .id(1L)
                    .itemId(2L)
                    .name("Item Name")
                    .ownerId(3L)
                    .build();

            assertThat(answerDto.getId()).isEqualTo(1L);
            assertThat(answerDto.getItemId()).isEqualTo(2L);
            assertThat(answerDto.getName()).isEqualTo("Item Name");
            assertThat(answerDto.getOwnerId()).isEqualTo(3L);
        }

        @Test
        void testBuilderPattern() {
            AnswerDto answerDto = AnswerDto.builder()
                    .id(1L)
                    .itemId(2L)
                    .name("Item Name")
                    .ownerId(3L)
                    .build();

            assertThat(answerDto.getId()).isEqualTo(1L);
            assertThat(answerDto.getItemId()).isEqualTo(2L);
            assertThat(answerDto.getName()).isEqualTo("Item Name");
            assertThat(answerDto.getOwnerId()).isEqualTo(3L);
        }

        @Test
        void testSetters() {
            AnswerDto answerDto = new AnswerDto();
            answerDto.setId(1L);
            answerDto.setItemId(2L);
            answerDto.setName("Item Name");
            answerDto.setOwnerId(3L);

            assertThat(answerDto.getId()).isEqualTo(1L);
            assertThat(answerDto.getItemId()).isEqualTo(2L);
            assertThat(answerDto.getName()).isEqualTo("Item Name");
            assertThat(answerDto.getOwnerId()).isEqualTo(3L);
        }

        @Test
        void testToString() {
            AnswerDto answerDto = new AnswerDto(1L, 2L, "Item Name", 3L);
            String expectedString = "AnswerDto{id=1, itemId=2, name=Item Name, ownerId=3}";

            assertThat(answerDto.toString()).isEqualTo(expectedString);
        }

        @Test
        void testEqualsAndHashCode() {
            AnswerDto answerDto1 = new AnswerDto(1L, 2L, "Item Name", 3L);
            AnswerDto answerDto2 = new AnswerDto(1L, 2L, "Item Name", 3L);
            AnswerDto answerDto3 = new AnswerDto(2L, 3L, "Other Name", 4L);

            assertThat(answerDto1).isEqualTo(answerDto2);
            assertThat(answerDto1).isNotEqualTo(answerDto3);
            assertThat(answerDto1.hashCode()).isEqualTo(answerDto2.hashCode());
            assertThat(answerDto1.hashCode()).isNotEqualTo(answerDto3.hashCode());
        }
    }

    @Nested
    class GetRequestWithAnswersRspTest {

        @Test
        void testNoArgsConstructor() {
            GetRequestWithAnswersRsp response = new GetRequestWithAnswersRsp();
            assertThat(response).isNotNull();
        }

        @Test
        void testAllArgsConstructor() {
            Long id = 1L;
            String description = "Description";
            LocalDateTime createdAt = LocalDateTime.now();
            List<AnswerDto> items = List.of(new AnswerDto());

            GetRequestWithAnswersRsp response = GetRequestWithAnswersRsp.builder()
                    .id(id)
                    .description(description)
                    .createdAt(createdAt)
                    .items(items)
                    .build();

            assertThat(response.getId()).isEqualTo(id);
            assertThat(response.getDescription()).isEqualTo(description);
            assertThat(response.getCreatedAt()).isEqualTo(createdAt);
            assertThat(response.getItems()).isEqualTo(items);
        }

        @Test
        void testBuilderPattern() {
            Long id = 1L;
            String description = "Description";
            LocalDateTime createdAt = LocalDateTime.now();
            List<AnswerDto> items = List.of(new AnswerDto());

            GetRequestWithAnswersRsp response = GetRequestWithAnswersRsp.builder()
                    .id(id)
                    .description(description)
                    .createdAt(createdAt)
                    .items(items)
                    .build();

            assertThat(response.getId()).isEqualTo(id);
            assertThat(response.getDescription()).isEqualTo(description);
            assertThat(response.getCreatedAt()).isEqualTo(createdAt);
            assertThat(response.getItems()).isEqualTo(items);
        }

        @Test
        void testSetters() {
            GetRequestWithAnswersRsp response = new GetRequestWithAnswersRsp();
            response.setId(1L);
            response.setDescription("Description");
            response.setCreatedAt(LocalDateTime.now());
            response.setItems(List.of(new AnswerDto()));

            assertThat(response.getId()).isEqualTo(1L);
            assertThat(response.getDescription()).isEqualTo("Description");
            assertThat(response.getCreatedAt()).isNotNull(); // Проверим, что дата установлена
            assertThat(response.getItems()).isNotNull();
        }

        @Test
        void testToString() {
            Long id = 1L;
            String description = "Description";
            LocalDateTime createdAt = LocalDateTime.now();
            List<AnswerDto> items = List.of(new AnswerDto(1L, 2L, "name", 3L));

            GetRequestWithAnswersRsp response = new GetRequestWithAnswersRsp(id, description, createdAt, items);

            String expectedString = "GetRequestWithAnswersRsp(id=1, description=Description, createdAt="
                    + createdAt + ", items=[AnswerDto{id=1, itemId=2, name=name, ownerId=3}])";

            assertThat(response.toString()).isEqualTo(expectedString);
        }

        @Test
        void testEqualsAndHashCode() {
            Long id = 1L;
            String description = "Description";
            LocalDateTime createdAt = LocalDateTime.now();
            List<AnswerDto> items = List.of(new AnswerDto(1L, 2L, "name", 3L));

            GetRequestWithAnswersRsp response1 = new GetRequestWithAnswersRsp(id, description, createdAt, items);
            GetRequestWithAnswersRsp response2 = new GetRequestWithAnswersRsp(id, description, createdAt, items);
            GetRequestWithAnswersRsp response3 = new GetRequestWithAnswersRsp(2L, "Other", createdAt.plusDays(1),
                    List.of());

            assertThat(response1).isEqualTo(response2);
            assertThat(response1).isNotEqualTo(response3);
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
            assertThat(response1.hashCode()).isNotEqualTo(response3.hashCode());
        }
    }
}
