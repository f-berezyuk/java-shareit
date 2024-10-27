package ru.practicum.shareit.item;

import java.sql.Timestamp;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Answer;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemModelsTest {
    @Nested
    class CommentTest {

        @Test
        void testCommentCreation() {
            Long id = 1L;
            Item item = new Item();
            User author = new User();
            String commentText = "This is a test comment";
            Timestamp created = new Timestamp(System.currentTimeMillis());

            Comment comment = Comment.builder()
                    .id(id)
                    .item(item)
                    .author(author)
                    .comment(commentText)
                    .created(created)
                    .build();

            assertThat(comment.getId()).isEqualTo(id);
            assertThat(comment.getItem()).isEqualTo(item);
            assertThat(comment.getAuthor()).isEqualTo(author);
            assertThat(comment.getComment()).isEqualTo(commentText);
            assertThat(comment.getCreated()).isEqualTo(created);
        }

        @Test
        void testToString() {
            Item item = new Item();
            User author = new User();
            String commentText = "This is a test comment";
            Timestamp created = new Timestamp(System.currentTimeMillis());

            Comment comment = new Comment(1L, item, author, commentText, created);

            // Test if toString() is not null and contains key attribute values
            assertThat(comment.toString()).isNotNull();
            assertThat(comment.toString()).contains("1", commentText);
        }
    }

    @Nested
    class ItemTest {

        @Test
        void testItemCreation() {
            Long id = 1L;
            Long ownerId = 2L;
            String name = "Test Item";
            String description = "This is a test item";
            Boolean available = true;
            List<Comment> comments = List.of(new Comment());
            List<Answer> answers = List.of(new Answer());

            Item item = new Item();
            item.setId(id);
            item.setOwnerId(ownerId);
            item.setName(name);
            item.setDescription(description);
            item.setAvailable(available);
            item.setComments(comments);
            item.setAnswers(answers);

            assertThat(item.getId()).isEqualTo(id);
            assertThat(item.getOwnerId()).isEqualTo(ownerId);
            assertThat(item.getName()).isEqualTo(name);
            assertThat(item.getDescription()).isEqualTo(description);
            assertThat(item.getAvailable()).isEqualTo(available);
            assertThat(item.getComments()).isEqualTo(comments);
            assertThat(item.getAnswers()).isEqualTo(answers);
        }

        @Test
        void testEqualsAndHashCode() {
            Long id1 = 1L;
            Long id2 = 2L;

            Item item1 = new Item();
            item1.setId(id1);

            Item item2 = new Item();
            item2.setId(id1);

            // Проверка равенства для объектов с одинаковым id
            assertThat(item1).isEqualTo(item2);
            assertThat(item1.hashCode()).isEqualTo(item2.hashCode());

            Item item3 = new Item();
            item3.setId(id2);

            // Проверка неравенства для объектов с разными id
            assertThat(item1).isNotEqualTo(item3);
        }

        @Test
        void testToString() {
            Item item = new Item();
            item.setId(1L);
            item.setName("Test Item");

            // Проверка, что toString() не возвращает null и содержит ключевые значения
            assertThat(item.toString()).isNotNull();
            assertThat(item.toString()).contains("1", "Test Item");
        }

    }
}
