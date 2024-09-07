package ru.practicum.shareit.item.storage;

import java.util.List;

import ru.practicum.shareit.item.model.Item;

public interface ItemRepository {
    Item add(Item newItem);

    Item findById(Long itemId);

    Item updateItem(Item item);

    List<Item> findAllByUser(Long userId);

    List<Item> searchLikeByNameOrByDescription(String text);
}
