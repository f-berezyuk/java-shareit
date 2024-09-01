package ru.practicum.shareit.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    HashMap<Long, Item> internalStorage = new HashMap<>();
    HashMap<Long, List<Long>> userIndex = new HashMap<>();
    private Long newId = 1L;

    @Override
    public Item add(Item newItem) {
        return addNewInternal(newItem);
    }

    private Item addNewInternal(Item newItem) {
        return saveInternal(getNewId(), newItem);
    }

    private Item saveInternal(Long id, Item item) {
        item.setId(id);
        internalStorage.put(id, item);

        Long ownerId = item.getOwnerId();
        if (!userIndex.containsKey(ownerId)) {
            userIndex.put(ownerId, new ArrayList<>());
        }

        userIndex.get(ownerId).add(id);

        return internalStorage.get(id);
    }

    private Long getNewId() {
        while (internalStorage.containsKey(newId)) {
            newId++;
        }

        return newId;
    }

    @Override
    public Item findById(Long itemId) {
        return internalStorage.get(itemId);
    }

    @Override
    public Item updateItem(Item item) {
        return saveInternal(item.getId(), item);
    }

    @Override
    public List<Item> findAllByUser(Long userId) {
        return userIndex.get(userId).stream().map(internalStorage::get).collect(Collectors.toList());
    }

    @Override
    public List<Item> searchLikeByNameOrByDescription(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }

        String textNormalized = text.toUpperCase();
        Collection<Item> values = internalStorage.values().stream().filter(Item::getAvailable).toList();
        Set<Long> result = new HashSet<>(values.stream()
                .filter(i -> i.getName().toUpperCase().contains(textNormalized))
                .map(Item::getId)
                .toList());

        result.addAll(values.stream()
                .filter(i -> i.getDescription().toUpperCase().contains(textNormalized))
                .map(Item::getId)
                .toList());

        return result.stream().map(internalStorage::get).collect(Collectors.toList());
    }
}
