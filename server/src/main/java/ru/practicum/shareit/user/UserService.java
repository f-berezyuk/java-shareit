package ru.practicum.shareit.user;

import java.util.List;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto saveUser(UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto);

    UserDto getUser(Long userId);

    UserDto deleteUser(Long userId);
}
