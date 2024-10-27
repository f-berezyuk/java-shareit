package ru.practicum.shareit.request;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.CreateItemRequest;
import ru.practicum.shareit.request.dto.GetRequestWithAnswersRsp;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestBody CreateItemRequest request) {
        return requestService.createRequest(request, userId);
    }

    @GetMapping
    public List<GetRequestWithAnswersRsp> getRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getRequests(userId);
    }

    @GetMapping("/all")
    public Page<ItemRequest> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestParam(required = false, value = "0") int from,
                                    @RequestParam(required = false, value = "10") int size) {
        return requestService.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@PathVariable Long requestId) {
        return requestService.getRequest(requestId);
    }
}
