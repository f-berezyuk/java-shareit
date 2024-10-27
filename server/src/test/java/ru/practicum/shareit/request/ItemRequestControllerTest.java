package ru.practicum.shareit.request;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import ru.practicum.shareit.request.dto.CreateItemRequest;
import ru.practicum.shareit.request.dto.GetRequestWithAnswersRsp;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {

    @Mock
    private ItemRequestService requestService;

    @InjectMocks
    private ItemRequestController controller;

    @Captor
    private ArgumentCaptor<Long> userIdCaptor;

    @Captor
    private ArgumentCaptor<CreateItemRequest> createItemRequestCaptor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRequest() {
        Long userId = 1L;
        CreateItemRequest request = new CreateItemRequest();
        request.setDescription("dsad");
        ItemRequestDto responseDto = ItemRequestDto.builder().build();

        when(requestService.createRequest(any(CreateItemRequest.class), anyLong())).thenReturn(responseDto);

        ItemRequestDto result = controller.createRequest(userId, request);

        // Verify service call
        verify(requestService, times(1)).createRequest(createItemRequestCaptor.capture(), userIdCaptor.capture());

        // Check arguments passed to the service
        assertThat(createItemRequestCaptor.getValue()).isEqualTo(request);
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);

        // Check returned value
        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    void testGetRequests() {
        Long userId = 1L;
        List<GetRequestWithAnswersRsp> responseList = List.of(new GetRequestWithAnswersRsp());

        when(requestService.getRequests(anyLong())).thenReturn(responseList);

        List<GetRequestWithAnswersRsp> result = controller.getRequests(userId);

        // Verify service call
        verify(requestService, times(1)).getRequests(userIdCaptor.capture());

        // Check argument passed to the service
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);

        // Check returned value
        assertThat(result).isEqualTo(responseList);
    }

    @Test
    void testGetAll() {
        Long userId = 1L;
        int from = 0;
        int size = 10;
        Page<ItemRequest> responsePage = Page.empty(); // Assuming you use some kind of Page.empty() or mock(Page.class)

        when(requestService.getAll(anyLong(), anyInt(), anyInt())).thenReturn(responsePage);

        Page<ItemRequest> result = controller.getAll(userId, from, size);

        // Verify service call
        verify(requestService, times(1)).getAll(userIdCaptor.capture(), eq(from), eq(size));

        // Check argument passed to the service
        assertThat(userIdCaptor.getValue()).isEqualTo(userId);

        // Check returned value
        assertThat(result).isEqualTo(responsePage);
    }

    @Test
    void testGetRequest() {
        Long requestId = 1L;
        ItemRequestDto responseDto = new ItemRequestDto();

        when(requestService.getRequest(anyLong())).thenReturn(responseDto);

        ItemRequestDto result = controller.getRequest(requestId);
        verify(requestService, times(1)).getRequest(requestId);

        assertThat(result).isEqualTo(responseDto);
    }
}
