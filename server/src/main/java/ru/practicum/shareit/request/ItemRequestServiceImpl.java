package ru.practicum.shareit.request;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.dto.CreateItemRequest;
import ru.practicum.shareit.request.dto.GetRequestWithAnswersRsp;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.model.Answer;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.AnswersRepository;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    AnswersRepository answersRepository;
    ItemRequestRepository requestRepository;
    UserService userService;
    ItemService itemService;

    @Override
    public ItemRequestDto createRequest(CreateItemRequest request, Long userId) {
        User user = userService.getOrThrow(userId);
        ItemRequest itemRequest = ItemRequest.builder()
                .user(user)
                .description(request.getDescription())
                .created(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        return ItemRequestMapper.toDto(requestRepository.save(itemRequest));
    }

    @Override
    public Page<ItemRequest> getAll(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("created").descending());

        return requestRepository.findByUserIdNotOrderByCreatedDesc(userId, pageable);
    }

    @Override
    public List<GetRequestWithAnswersRsp> getRequests(Long userId) {
        List<ItemRequest> itemRequests = requestRepository.findByUserIdOrderByCreatedDesc(userId);
        return itemRequests.stream().map(ItemRequestMapper::toResponse).toList();
    }

    public ItemRequestDto getRequest(Long requestId) {
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new ItemRequestNotFoundException("Request not found"));
        return ItemRequestMapper.toDto(itemRequest);
    }

    @Override
    public void answer(Long id, Long requestId) {
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new ItemRequestNotFoundException("Request not found"));

        Answer answer = Answer.builder()
                .item(itemService.getOrThrow(id))
                .itemRequest(itemRequest)
                .build();

        answersRepository.save(answer);
    }
}
