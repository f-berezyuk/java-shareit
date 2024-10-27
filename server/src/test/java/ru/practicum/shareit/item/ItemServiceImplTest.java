package ru.practicum.shareit.item;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.EBookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UserNotAuthorizedException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private UserDto createUserDto(long id) {
        return new UserDto(id, "User", "user@example.com");
    }

    private ItemDto createItemDto() {
        return ItemDto.builder()
                .id(1L)
                .name("Item Name")
                .description("Description")
                .available(true)
                .build();
    }

    private Item createItem(long ownerId) {
        return Item.builder()
                .id(1L)
                .name("Item Name")
                .description("Description")
                .available(true)
                .ownerId(ownerId)
                .build();
    }

    @Test
    void createItem_Success() {
        Long userId = 1L;
        ItemDto itemDto = createItemDto();

        when(userService.getUser(userId)).thenReturn(createUserDto(userId));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item item = invocation.getArgument(0);
            item.setId(1L);
            return item;
        });

        ItemDto result = itemService.createItem(userId, itemDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(userService, times(1)).getUser(userId);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void authUser_Success() {
        Long userId = 1L;
        Long itemId = 1L;
        UserDto user = createUserDto(userId);
        Item item = createItem(userId);

        when(userService.getUser(userId)).thenReturn(user);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        itemService.authUser(userId, itemId);

        verify(userService, times(1)).getUser(userId);
    }

    @Test
    void authUser_ThrowsUserNotAuthorizedException() {
        Long userId = 1L;
        Long itemId = 1L;
        UserDto user = createUserDto(2L); // User id different from item owner
        Item item = createItem(1L);

        when(userService.getUser(userId)).thenReturn(user);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> itemService.authUser(userId, itemId))
                .isInstanceOf(UserNotAuthorizedException.class)
                .hasMessage("Пользователь не является владельцем предмета");
    }

    @Test
    void getOrThrow_ItemFound() {
        Item item = createItem(1L);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Item result = itemService.getOrThrow(1L);

        assertThat(result).isEqualTo(item);
    }

    @Test
    void getOrThrow_ItemNotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.getOrThrow(1L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("Предмет с идентификатором 1 не найден");
    }

    @Test
    void updateItem_Success() {
        Long itemId = 1L;
        ItemDto itemDto = createItemDto();
        Item item = createItem(1L);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemDto result = itemService.updateItem(itemId, itemDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(itemDto.getName());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void findItem_Success() {
        Item item = createItem(1L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ItemDto result = itemService.findItem(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Item Name");
    }

    @Test
    void findAllByUser_Success() {
        Long userId = 1L;
        List<Item> items = List.of(createItem(1L));
        when(itemRepository.findAllByOwnerId(userId)).thenReturn(items);

        List<ItemDto> result = itemService.findAllByUser(userId);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Item Name");
    }

    @Test
    void findItemsByText_Success() {
        String text = "Item";
        List<Item> items = List.of(createItem(1L));
        when(itemRepository.findAllByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCaseAndAvailableIsTrue(text))
                .thenReturn(items);

        List<ItemDto> result = itemService.findItemsByText(text);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Item Name");
    }

    @Test
    void findItemsByText_EmptySearchString() {
        List<ItemDto> result = itemService.findItemsByText("");

        assertThat(result).isEmpty();
    }

    @Test
    void commentItem_Success() {
        Long itemId = 1L;
        Long authorId = 1L;
        String text = "Great item!";
        Item item = createItem(1L);
        UserDto authorDto = createUserDto(authorId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userService.getUser(authorId)).thenReturn(authorDto);
        when(bookingRepository.findAllByBookerIdAndItemIdAndStatusAndEndAtBefore(eq(authorId), eq(itemId),
                eq(EBookingStatus.APPROVED), any(Timestamp.class)))
                .thenReturn(List.of());

        assertThatThrownBy(() -> itemService.commentItem(itemId, authorId, text))
                .isInstanceOf(UserNotAuthorizedException.class)
                .hasMessage("Пользователь не арендовал данный предмет");

        when(bookingRepository.findAllByBookerIdAndItemIdAndStatusAndEndAtBefore(eq(authorId), eq(itemId),
                eq(EBookingStatus.APPROVED), any(Timestamp.class)))
                .thenReturn(List.of(mock(Booking.class)));

        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment comment = invocation.getArgument(0);
            comment.setId(1L);
            return comment;
        });

        CommentDto result = itemService.commentItem(itemId, authorId, text);

        assertThat(result).isNotNull();
        assertThat(result.getText()).isEqualTo(text);
    }
}
