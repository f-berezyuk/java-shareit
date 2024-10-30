package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUser() {
        Long userId = 1L;
        UserDto expectedUser = new UserDto();
        expectedUser.setId(userId);
        expectedUser.setName("User Name");

        when(userService.getUser(userId)).thenReturn(expectedUser);

        UserDto actualUser = userController.getUser(userId);

        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getId()).isEqualTo(expectedUser.getId());
        assertThat(actualUser.getName()).isEqualTo(expectedUser.getName());
        verify(userService, times(1)).getUser(userId);
    }

    @Test
    void testGetAllUsers() {
        List<UserDto> expectedUsers = List.of(
                new UserDto(1L, "User One", "user1@example.com"),
                new UserDto(2L, "User Two", "user2@example.com")
        );

        when(userService.getAllUsers()).thenReturn(expectedUsers);

        List<UserDto> actualUsers = userController.getAllUsers();

        assertThat(actualUsers).isNotNull();
        assertThat(actualUsers.size()).isEqualTo(expectedUsers.size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testSaveNewUser() {
        UserDto newUserDto = new UserDto(null, "New User", "newuser@example.com");
        UserDto savedUserDto = new UserDto(1L, "New User", "newuser@example.com");

        when(userService.saveUser(newUserDto)).thenReturn(savedUserDto);

        UserDto actualUserDto = userController.saveNewUser(newUserDto);

        assertThat(actualUserDto).isNotNull();
        assertThat(actualUserDto.getId()).isEqualTo(savedUserDto.getId());
        verify(userService, times(1)).saveUser(newUserDto);
    }

    @Test
    void testUpdateUser() {
        Long userId = 1L;
        UserDto updateDto = new UserDto(null, "Updated User", "updated@example.com");
        UserDto updatedUserDto = new UserDto(userId, "Updated User", "updated@example.com");

        when(userService.updateUser(userId, updateDto)).thenReturn(updatedUserDto);

        UserDto actualUserDto = userController.updateUser(updateDto, userId);

        assertThat(actualUserDto).isNotNull();
        assertThat(actualUserDto.getName()).isEqualTo(updateDto.getName());
        verify(userService, times(1)).updateUser(userId, updateDto);
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;
        UserDto deletedUserDto = new UserDto(userId, "Deleted User", "deleted@example.com");

        when(userService.deleteUser(userId)).thenReturn(deletedUserDto);

        UserDto actualUserDto = userController.deleteUser(userId);

        assertThat(actualUserDto).isNotNull();
        assertThat(actualUserDto.getId()).isEqualTo(userId);
        verify(userService, times(1)).deleteUser(userId);
    }
}