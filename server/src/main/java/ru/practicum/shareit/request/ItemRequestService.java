package ru.practicum.shareit.request;

import java.util.List;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.request.dto.CreateItemRequest;
import ru.practicum.shareit.request.dto.GetRequestWithAnswersRsp;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

public interface ItemRequestService {
    ItemRequestDto createRequest(CreateItemRequest request, Long userId);

    Page<ItemRequest> getAll(Long userId, int from, int size);

    List<GetRequestWithAnswersRsp> getRequests(Long userId);

    ItemRequestDto getRequest(Long requestId);

    void answer(Long id, Long requestId);
}
