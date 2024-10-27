package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentReq;
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody @Valid ItemDto itemDto) {
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody ItemDto itemDto,
                                             @PathVariable Long itemId) {
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId) {
        return itemClient.findItem(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.findAllByUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItemsByText(@RequestParam String text) {
        return itemClient.findItemsByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> comment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long itemId,
                                          @RequestBody CommentReq comment) {
        return itemClient.commentItem(itemId, userId, comment);
    }
}
