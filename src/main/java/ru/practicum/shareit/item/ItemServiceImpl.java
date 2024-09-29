package ru.practicum.shareit.item;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.EBookingStatus;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.UserNotAuthorizedException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    private final UserService userService;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        userService.getUser(userId);
        Item newItem = ItemMapper.toEntity(itemDto);
        newItem.setOwnerId(userId);
        return ItemMapper.toDto(repository.save(newItem));
    }

    @Override
    public void authUser(Long userId, Long itemId) throws UserNotAuthorizedException {
        UserDto user = userService.getUser(userId);
        Item item = getOrThrow(itemId);

        if (!user.getId().equals(item.getOwnerId())) {
            throw new UserNotAuthorizedException("Пользователь не является владельцем предмета");
        }
    }

    private Item getOrThrow(Long itemId) {
        Optional<Item> user = repository.findById(itemId);
        if (user.isPresent()) {
            return user.get();
        }

        throw new UserNotFoundException("Предмет с идентификатором " + itemId + " не найден");
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto) {
        Item item = getOrThrow(itemId);
        item.setAvailable(Optional.ofNullable(itemDto.getAvailable()).orElse(item.getAvailable()));
        item.setName(Optional.ofNullable(itemDto.getName()).orElse(item.getName()));
        item.setDescription(Optional.ofNullable(itemDto.getDescription()).orElse(item.getDescription()));
        return ItemMapper.toDto(repository.save(item));
    }

    @Override
    public ItemDto findItem(Long itemId) {
        return ItemMapper.toDto(getOrThrow(itemId));
    }

    @Override
    public List<ItemDto> findAllByUser(Long userId) {
        return ItemMapper.toDto(repository.findAllByOwnerId(userId));
    }

    @Override
    public List<ItemDto> findItemsByText(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        List<Item> items = repository.findAllByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCaseAndAvailableIsTrue(text);
        return ItemMapper.toDto(items);
    }

    @Override
    public CommentDto commentItem(Long itemId, Long authorId, String text) {
        Item item = getOrThrow(itemId);
        User author = UserMapper.toEntity(userService.getUser(authorId));
        if (bookingRepository.findAllByBookerIdAndItemIdAndStatusAndEndAtBefore(authorId,
                        itemId,
                        EBookingStatus.APPROVED,
                        Timestamp.valueOf(LocalDateTime.now()))
                .isEmpty()) {
            throw new BookingNotFoundException("Пользователь не арендовал данный предмет");
        }
        Comment comment = Comment.builder()
                .author(author)
                .item(item)
                .comment(text)
                .created(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        return ItemMapper.toDto(commentRepository.save(comment));
    }
}
