package ru.practicum.shareit.item;

import java.util.List;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.UserNotAuthorizedException;

@Service
public interface ItemService {
    ItemDto createItem(Long userId, ItemDto itemDto);

    void authUser(Long userId, Long id) throws UserNotAuthorizedException;

    ItemDto updateItem(Long itemId, ItemDto itemDto);

    ItemDto findItem(Long itemId);

    List<ItemDto> findAllByUser(Long userId);

    List<ItemDto> findItemsByText(String text);
}