package ru.practicum.shareit.user.storage;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final HashMap<Long, User> internalStorage = new HashMap<>();
    private final HashMap<String, Long> emailIndex = new HashMap<>();
    private Long newId = 1L;


    @Override
    public List<User> findAll() {
        return internalStorage.values().stream().toList();
    }

    @Override
    public User add(User user) {
        return saveNewInternal(user);
    }

    @Override
    public User findById(Long userId) {
        return internalStorage.get(userId);
    }

    @Override
    public User updateById(Long userId, User user) {
        return saveInternal(userId, user);
    }

    @Override
    public void deleteById(Long userId) {
        deleteByIdInternal(userId);
    }

    @Override
    public User findByEmail(String email) {
        Long key = emailIndex.get(email);
        return key == null ? null : internalStorage.get(key);
    }

    private void deleteByIdInternal(Long userId) {
        User removed = internalStorage.get(userId);
        internalStorage.remove(userId);
        emailIndex.remove(removed.getEmail());
    }

    private User saveNewInternal(User user) {
        return saveInternal(getNewId(), user);
    }

    private User saveInternal(Long id, User user) {
        user.setId(id);
        internalStorage.put(id, user);
        emailIndex.put(user.getEmail(), id);
        return internalStorage.get(id);
    }

    private Long getNewId() {
        while (internalStorage.containsKey(newId)) {
            newId++;
        }
        return newId;
    }
}
