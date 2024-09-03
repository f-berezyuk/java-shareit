package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.UserNotAuthorizedException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    private final UserService userService;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        userService.getUser(userId);
        Item newItem = ItemMapper.toEntity(itemDto);
        newItem.setOwnerId(userId);
        return ItemMapper.toDto(repository.add(newItem));
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
        Item user = repository.findById(itemId);
        if (user != null) {
            return user;
        }

        throw new UserNotFoundException("Предмет с идентификатором " + itemId + " не найден");
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto) {
        Item item = getOrThrow(itemId);
        item.setAvailable(Optional.ofNullable(itemDto.getAvailable()).orElse(item.getAvailable()));
        item.setName(Optional.ofNullable(itemDto.getName()).orElse(item.getName()));
        item.setDescription(Optional.ofNullable(itemDto.getDescription()).orElse(item.getDescription()));
        return ItemMapper.toDto(repository.updateItem(item));
    }

    @Override
    public ItemDto findItem(Long itemId) {
        return ItemMapper.toDto(getOrThrow(itemId));
    }

    @Override
    public List<ItemDto> findAllByUser(Long userId) {
        return ItemMapper.toDto(repository.findAllByUser(userId));
    }

    @Override
    public List<ItemDto> findItemsByText(String text) {
        return ItemMapper.toDto(repository.searchLikeByNameOrByDescription(text));
    }
}
