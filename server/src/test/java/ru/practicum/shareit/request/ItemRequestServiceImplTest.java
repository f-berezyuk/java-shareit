package ru.practicum.shareit.request;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ItemRequestServiceImplTest {

    @Mock
    private AnswersRepository answersRepository;
    @Mock
    private ItemRequestRepository requestRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemRequestServiceImpl requestService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRequest() {
        Long userId = 1L;
        CreateItemRequest createRequest = new CreateItemRequest("New Request");

        User user = new User();
        user.setId(userId);

        ItemRequest itemRequest = ItemRequest.builder()
                .user(user)
                .description(createRequest.getDescription())
                .created(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        when(userService.getOrThrow(userId)).thenReturn(user);
        when(requestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDto result = requestService.createRequest(createRequest, userId);

        assertThat(result.getDescription()).isEqualTo(createRequest.getDescription());
        verify(requestRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    void testGetAll() {
        Long userId = 1L;
        LocalDateTime created = LocalDateTime.of(2023, 10, 1, 12, 0);
        Item item = new Item(1L, 1L, "Item Name", "Item Description", true, null, null);
        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("Description")
                .created(Timestamp.valueOf(created))
                .build();
        Answer answer = new Answer(1L, item, request);
        request.setAnswers(List.of(answer));
        Page<ItemRequest> page = new PageImpl<>(List.of(request));

        when(requestRepository.findByUserIdNotOrderByCreatedDesc(any(), any())).thenReturn(page);

        Page<ItemRequest> result = requestService.getAll(userId, 0, 10);

        assertThat(result.getContent().size()).isEqualTo(1);
    }

    @Test
    void testGetRequests() {
        Long userId = 1L;
        LocalDateTime created = LocalDateTime.of(2023, 10, 1, 12, 0);
        Item item = new Item(1L, 1L, "Item Name", "Item Description", true, null, null);
        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("Description")
                .created(Timestamp.valueOf(created))
                .build();
        Answer answer = new Answer(1L, item, request);
        request.setAnswers(List.of(answer));

        when(requestRepository.findByUserIdOrderByCreatedDesc(userId)).thenReturn(List.of(request));

        List<GetRequestWithAnswersRsp> result = requestService.getRequests(userId);

        assertThat(result.size()).isEqualTo(1);
        verify(requestRepository, times(1)).findByUserIdOrderByCreatedDesc(userId);
    }

    @Test
    void testGetRequest() {
        Long requestId = 1L;
        LocalDateTime created = LocalDateTime.of(2023, 10, 1, 12, 0);
        Item item = new Item(1L, 1L, "Item Name", "Item Description", true, null, null);
        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("Description")
                .created(Timestamp.valueOf(created))
                .build();
        Answer answer = new Answer(1L, item, request);
        request.setAnswers(List.of(answer));

        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));

        ItemRequestDto result = requestService.getRequest(requestId);

        assertThat(result.getId()).isEqualTo(requestId);
    }

    @Test
    void testGetRequestNotFound() {
        Long requestId = 1L;

        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(ItemRequestNotFoundException.class, () -> requestService.getRequest(requestId));
    }

    @Test
    void testAnswer() {
        Long requestId = 1L;
        Long itemId = 2L;
        LocalDateTime created = LocalDateTime.of(2023, 10, 1, 12, 0);
        Item item = new Item(2L, 1L, "Item Name", "Item Description", true, null, null);
        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("Description")
                .created(Timestamp.valueOf(created))
                .build();
        Answer answer = new Answer(1L, item, request);
        request.setAnswers(List.of(answer));
        request.setId(requestId);

        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(itemService.getOrThrow(itemId)).thenReturn(item);
        when(answersRepository.save(any(Answer.class))).thenReturn(answer);

        requestService.answer(itemId, requestId);

        verify(answersRepository, times(1)).save(any(Answer.class));
    }
}
