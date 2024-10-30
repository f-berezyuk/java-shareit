package ru.practicum.shareit.user;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserEmailDuplicateException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUser_UserFound() {
        // Setup
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("User Name");
        user.setEmail("user@example.com");

        when(repository.findById(userId)).thenReturn(Optional.of(user));

        // Execute
        UserDto userDto = userService.getUser(userId);

        // Verify
        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(userId);
        verify(repository, times(1)).findById(userId);
    }

    @Test
    void testGetUser_UserNotFound() {
        Long userId = 1L;
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(userId));

        verify(repository, times(1)).findById(userId);
    }

    @Test
    void testSaveUser_EmailDuplicate() {
        UserDto userDto = new UserDto();
        userDto.setEmail("duplicate@example.com");

        User duplicateUser = new User();
        duplicateUser.setEmail("duplicate@example.com");

        when(repository.findByEmailIgnoreCase(userDto.getEmail())).thenReturn(duplicateUser);

        assertThrows(UserEmailDuplicateException.class, () -> userService.saveUser(userDto));

        verify(repository, times(1)).findByEmailIgnoreCase(userDto.getEmail());
    }

    @Test
    void testSaveUser_Success() {
        UserDto userDto = new UserDto();
        userDto.setEmail("user@example.com");
        userDto.setName("User Name");

        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setEmail("user@example.com");
        expectedUser.setName("User Name");

        when(repository.findByEmailIgnoreCase(userDto.getEmail())).thenReturn(null);
        when(repository.save(any(User.class))).thenReturn(expectedUser);

        UserDto savedUserDto = userService.saveUser(userDto);

        assertThat(savedUserDto.getId()).isEqualTo(expectedUser.getId());
        assertThat(savedUserDto.getEmail()).isEqualTo(expectedUser.getEmail());
        verify(repository, times(1)).findByEmailIgnoreCase(userDto.getEmail());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_Success() {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setName("Updated Name");
        userDto.setEmail("updated@example.com");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Old Name");
        existingUser.setEmail("old@example.com");

        when(repository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        UserDto updatedUserDto = userService.updateUser(userId, userDto);

        assertThat(updatedUserDto.getName()).isEqualTo(userDto.getName());
        assertThat(updatedUserDto.getEmail()).isEqualTo(userDto.getEmail());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(repository.findById(userId)).thenReturn(Optional.of(user));

        UserDto removedUserDto = userService.deleteUser(userId);

        assertThat(removedUserDto.getId()).isEqualTo(userId);
        verify(repository, times(1)).deleteById(userId);
    }
}
