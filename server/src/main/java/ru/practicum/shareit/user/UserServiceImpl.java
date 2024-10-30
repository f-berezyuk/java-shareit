package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserEmailDuplicateException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto getUser(Long userId) {
        return UserMapper.toDto(getOrThrow(userId));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll().stream().map(UserMapper::toDto).toList();
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        checkEmail(userDto);
        User user = UserMapper.toEntity(userDto);
        return UserMapper.toDto(repository.save(user));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        checkEmail(userDto);

        User oldUser = getOrThrow(userId);

        oldUser.setName(Optional.ofNullable(userDto.getName()).orElse(oldUser.getName()));
        oldUser.setEmail(Optional.ofNullable(userDto.getEmail()).orElse(oldUser.getEmail()));

        User user = repository.save(oldUser);

        return UserMapper.toDto(user);
    }

    @Override
    public UserDto deleteUser(Long userId) {
        User removed = getOrThrow(userId);
        repository.deleteById(userId);

        return UserMapper.toDto(removed);
    }

    @Override
    public User getOrThrow(Long userId) {
        Optional<User> user = repository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }

        throw new UserNotFoundException("Пользователь с идентификатором " + userId + " не найден");
    }

    private void checkEmail(UserDto userDto) {
        User user = repository.findByEmailIgnoreCase(userDto.getEmail());
        if (user != null) {
            throw new UserEmailDuplicateException(
                    "Пользователь с email " + userDto.getEmail() + " уже зарегистрирован.");
        }
    }
}
