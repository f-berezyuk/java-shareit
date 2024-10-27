package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentReq;
import ru.practicum.shareit.item.dto.ItemDto;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }


    public ResponseEntity<Object> createItem(Long userId, @Valid ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> updateItem(Long userId, Long itemId, ItemDto itemDto) {
        String path = "/" + itemId;
        return patch(path, userId, itemDto);
    }

    public ResponseEntity<Object> findItem(Long itemId) {
        String path = "/" + itemId;
        return get(path);
    }

    public ResponseEntity<Object> findAllByUser(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findItemsByText(String text) {
        return get("/search?text=" + text);
    }

    public ResponseEntity<Object> commentItem(Long itemId, Long userId, CommentReq comment) {
        String path = "/" + itemId + "/comment";
        return post(path, userId, comment);
    }
}
