package ru.practicum.shareit.request;

import java.util.List;

import ru.practicum.shareit.request.dto.CreateItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

public interface ItemRequestService {
    ItemRequestDto createRequest(CreateItemRequest request, Long userId);


    List<ItemRequestDto> getAll(Long userId);

    List<ItemRequestDto> getRequests(Long userId);

    ItemRequestDto getRequests(String requestId);

    void answer(Long id, Long requestId);
}
