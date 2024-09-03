package ru.practicum.shareit.user.storage;

import java.util.List;

import ru.practicum.shareit.user.model.User;

public interface UserRepository {
    List<User> findAll();

    User add(User user);

    User findById(Long userId);

    User updateById(Long userId, User user);

    void deleteById(Long userId);

    User findByEmail(String email);
}
