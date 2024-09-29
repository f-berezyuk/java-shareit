package ru.practicum.shareit.item;

import java.util.List;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.common.ErrorResponse;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentReq;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid ItemDto itemDto) {
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody ItemDto itemDto,
                              @PathVariable Long itemId) {
        itemService.authUser(userId, itemId);
        return itemService.updateItem(itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId) {
        return itemService.findItem(itemId);
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findAllByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemsByText(@RequestParam String text) {
        return itemService.findItemsByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto comment(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId,
                              @RequestBody CommentReq comment) {
        return itemService.commentItem(itemId, userId, comment.text);
    }


    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(final UserNotFoundException e) {
        return new ErrorResponse("Пользователя не существует.", e.getMessage());
    }
}
