package ru.practicum.shareit.gateway.item;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.gateway.item.dto.CommentPostDto;
import ru.practicum.shareit.gateway.item.dto.ItemPatchDto;
import ru.practicum.shareit.gateway.item.dto.ItemPostDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.gateway.common.CommonUtils.ITEMS_RESOURCE;
import static ru.practicum.shareit.gateway.common.CommonUtils.MAX_SIZE;
import static ru.practicum.shareit.gateway.common.CommonUtils.X_SHARER_USER_ID;

@Slf4j
@Controller
@RequestMapping(path = ITEMS_RESOURCE)
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> postItem(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @RequestBody @Valid ItemPostDto itemPostDto
    ) {
        log.info("postItem({})", itemPostDto);

        return itemClient.postItem(userId, itemPostDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> patchByItemId(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @PathVariable long itemId,
            @RequestBody @Valid ItemPatchDto itemPatchDto
    ) {
        log.info("patchByItemId(itemId = {}, {}, userId = {})", itemId, itemPatchDto, userId);

        return itemClient.patchByItemId(itemId, itemPatchDto, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getByItemIdAndUserId(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @PathVariable long itemId
    ) {
        log.info("getByItemIdAndUserId(itemId = {}, userId = {})", itemId, userId);

        return itemClient.getByItemIdAndUserId(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByOwnerId(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = MAX_SIZE) @Positive int size
    ) {
        log.info("getAllByOwnerId(userId = {}, from = {}, size = {})", userId, from, size);

        return itemClient.getAllByOwnerId(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getAllByNameOrDescriptionContaining(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = MAX_SIZE) @Positive int size,
            @RequestParam String text
    ) {
        log.info("getAllByNameOrDescriptionContaining(userId = {}, from = {}, size = {}, text = {})",
                userId,
                from,
                size,
                text
        );

        return itemClient.getAllByNameOrDescriptionContaining(userId, from, size, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @PathVariable long itemId,
            @RequestBody @Valid CommentPostDto commentPostDto
    ) {
        log.info("postComment(userId = {}, itemId = {}, {})", userId, itemId, commentPostDto);

        return itemClient.postComment(userId, itemId, commentPostDto);
    }

}
