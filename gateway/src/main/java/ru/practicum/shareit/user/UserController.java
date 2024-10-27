package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserClient userClient;

    @GetMapping({"/{userId}"})
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        return userClient.getUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<Object> saveNewUser(@RequestBody @Valid UserDto userDto) {
        return userClient.saveUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto, @PathVariable Long userId) {
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        return userClient.deleteUser(userId);
    }
}
