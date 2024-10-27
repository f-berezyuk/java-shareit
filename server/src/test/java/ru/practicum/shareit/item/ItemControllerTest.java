package ru.practicum.shareit.item;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.common.ErrorHandler;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentReq;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UserNotAuthorizedException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    @InjectMocks
    private ItemController controller;
    @Mock
    private ItemService itemService;

    private static ItemDto.ItemDtoBuilder getItemDtoBuilder(String text) {
        return ItemDto.builder().name("Item " + text).description("description" + text).available(true);
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(ErrorHandler.class)
                .build();
    }

    @Test
    void testCreateItem() throws Exception {
        Long userId = 1L;
        ItemDto itemDto = getItemDtoBuilder("1").build();
        ItemDto savedItemDto = getItemDtoBuilder("1").id(1L).build();

        when(itemService.createItem(eq(userId), any(ItemDto.class))).thenReturn(savedItemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedItemDto.getId()))
                .andExpect(jsonPath("$.name").value(savedItemDto.getName()));
    }

    @Test
    void testUpdateItem_Unauthorized() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDto itemDto = getItemDtoBuilder("1").build();

        doThrow(new UserNotAuthorizedException("")).when(itemService).authUser(userId, itemId);

        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest()) // Assuming your ErrorHandler maps this to HttpStatus.FORBIDDEN
                .andExpect(jsonPath("$.error").value("Доступ запрещён."));
    }

    @Test
    void testGetItem() throws Exception {
        Long itemId = 1L;
        ItemDto itemDto = getItemDtoBuilder("1").id(itemId).build();

        when(itemService.findItem(itemId)).thenReturn(itemDto);

        mockMvc.perform(get("/items/{itemId}", itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId))
                .andExpect(jsonPath("$.name").value(itemDto.getName()));
    }

    @Test
    void testGetItem_NotFound() throws Exception {
        Long itemId = 1L;
        doThrow(new ItemNotFoundException("Предмет не существует")).when(itemService).findItem(itemId);

        mockMvc.perform(get("/items/{itemId}", itemId))
                .andExpect(status().isNotFound()) // Assuming your ErrorHandler maps this to HttpStatus.NOT_FOUND
                .andExpect(jsonPath("$.error").value("Предмет не существует."));
    }

    @Test
    void testGetItems() throws Exception {
        Long userId = 1L;
        List<ItemDto> items = List.of(
                getItemDtoBuilder("1").id(1L).build(),
                getItemDtoBuilder("2").id(2L).build()
        );

        when(itemService.findAllByUser(userId)).thenReturn(items);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Item 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Item 2"));
    }

    @Test
    void testFindItemsByText() throws Exception {
        String searchText = "test";
        List<ItemDto> items = List.of(getItemDtoBuilder("test").id(1L).build());

        when(itemService.findItemsByText(searchText)).thenReturn(items);

        mockMvc.perform(get("/items/search")
                        .param("text", searchText))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Item test"));
    }

    @Test
    void testCommentItem() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        CommentReq commentReq = new CommentReq("Great item!");
        CommentDto commentDto = CommentDto.builder().id(1L).text("Great item!").build();

        when(itemService.commentItem(eq(itemId), eq(userId), any(String.class))).thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()));
    }
}