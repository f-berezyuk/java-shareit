package ru.practicum.shareit.item;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemMapperTest {

    @Test
    void testToDto_Item() {
        Item item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        item.setName("Item Name");
        item.setDescription("Item Description");

        ItemDto itemDto = ItemMapper.toDto(item);

        assertThat(itemDto.getId()).isEqualTo(item.getId());
        assertThat(itemDto.getAvailable()).isEqualTo(item.getAvailable());
        assertThat(itemDto.getName()).isEqualTo(item.getName());
        assertThat(itemDto.getDescription()).isEqualTo(item.getDescription());
        assertThat(itemDto.getComments()).isNull();  // Убедимся, что comments отсутствуют, так как не добавлены
    }

    @Test
    void testToDto_ItemWithComments() {
        Item item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        item.setName("Item Name");
        item.setDescription("Item Description");

        Comment comment = Comment.builder()
                .id(1L)
                .created(Timestamp.valueOf(LocalDateTime.now()))
                .comment("Sample Comment")
                .author(mock(User.class))
                .build();  // Используем mock для comment
        when(comment.getAuthor().getName()).thenReturn("Author");
        item.setComments(List.of(comment));

        ItemDto itemDto = ItemMapper.toDto(item);

        assertThat(itemDto.getComments()).isNotNull();
        assertThat(itemDto.getComments()).hasSize(1);
        assertThat(itemDto.getComments().getFirst().getId()).isEqualTo(1L);
    }

    @Test
    void testToEntity() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setAvailable(true);
        itemDto.setName("Item Name");
        itemDto.setDescription("Item Description");

        Item item = ItemMapper.toEntity(itemDto);

        assertThat(item.getId()).isEqualTo(itemDto.getId());
        assertThat(item.getAvailable()).isEqualTo(itemDto.getAvailable());
        assertThat(item.getName()).isEqualTo(itemDto.getName());
        assertThat(item.getDescription()).isEqualTo(itemDto.getDescription());
    }

    @Test
    void testToDto_ListOfItems() {
        Item item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        item.setName("Item Name");
        item.setDescription("Item Description");

        List<ItemDto> itemDtoList = ItemMapper.toDto(List.of(item));

        assertThat(itemDtoList).hasSize(1);
        assertThat(itemDtoList.getFirst().getId()).isEqualTo(item.getId());
    }

    @Test
    void testToDto_Comment() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setComment("Sample Comment");

        User author = new User();
        author.setName("Author Name");
        comment.setAuthor(author);

        comment.setCreated(Timestamp.valueOf(LocalDateTime.now()));

        CommentDto commentDto = ItemMapper.toDto(comment);

        assertThat(commentDto.getId()).isEqualTo(comment.getId());
        assertThat(commentDto.getText()).isEqualTo(comment.getComment());
        assertThat(commentDto.getAuthorName()).isEqualTo(comment.getAuthor().getName());
        assertThat(commentDto.getCreated()).isEqualTo(comment.getCreated().toLocalDateTime());
    }
}
