package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.CreateItemRequest;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody CreateItemRequest request) {
        return itemRequestClient.createRequest(request, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(required = false, value = "0") int from,
                                         @RequestParam(required = false, value = "10") int size) {
        return itemRequestClient.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@PathVariable Long requestId) {
        return itemRequestClient.getRequest(requestId);
    }
}
