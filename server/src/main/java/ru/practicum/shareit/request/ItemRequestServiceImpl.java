package ru.practicum.shareit.request;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.CreateItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
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
    public List<ItemRequestDto> getAll(Long userId) {
        return List.of();
    }

    @Override
    public List<ItemRequestDto> getRequests(Long userId) {
        return List.of();
    }

    @Override
    public ItemRequestDto getRequests(String requestId) {
        return null;
    }

    @Override
    public void answer(Long id, Long requestId) {

    }
}
