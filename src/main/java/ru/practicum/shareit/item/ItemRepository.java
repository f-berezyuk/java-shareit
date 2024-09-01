package ru.practicum.shareit.item;

import java.util.List;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

@Repository
public interface ItemRepository {
    Item add(Item newItem);

    Item findById(Long itemId);

    Item updateItem(Item item);

    List<Item> findAllByUser(Long userId);

    List<Item> searchLikeByNameOrByDescription(String text);
}
