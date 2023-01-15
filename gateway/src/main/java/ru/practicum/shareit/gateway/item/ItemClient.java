package ru.practicum.shareit.gateway.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.item.dto.CommentPostDto;
import ru.practicum.shareit.gateway.item.dto.ItemPatchDto;
import ru.practicum.shareit.gateway.item.dto.ItemPostDto;

import java.util.Map;

import static ru.practicum.shareit.gateway.common.CommonUtils.ITEMS_RESOURCE;
import static ru.practicum.shareit.gateway.common.CommonUtils.SHAREIT_SERVER_URL_PROPERTY;

@Service
public class ItemClient extends BaseClient {

    @Autowired
    public ItemClient(@Value(SHAREIT_SERVER_URL_PROPERTY) String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + ITEMS_RESOURCE))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> postItem(long userId, ItemPostDto itemPostDto) {
        return post("", userId, itemPostDto);
    }

    public ResponseEntity<Object> patchByItemId(long itemId, ItemPatchDto itemPatchDto, long userId) {
        return patch("/" + itemId, userId, itemPatchDto);
    }

    public ResponseEntity<Object> getByItemIdAndUserId(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllByOwnerId(long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getAllByNameOrDescriptionContaining(long userId, int from, int size, String text) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size,
                "text", text
        );
        return get("/search?from={from}&size={size}&text={text}", userId, parameters);
    }

    public ResponseEntity<Object> postComment(long userId, long itemId, CommentPostDto commentPostDto) {
        return post("/" + itemId + "/comment", userId, commentPostDto);
    }

}
