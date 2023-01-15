package ru.practicum.shareit.server.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.server.common.CommonUtils;
import ru.practicum.shareit.server.item.dto.CommentDto;
import ru.practicum.shareit.server.item.dto.CommentMapper;
import ru.practicum.shareit.server.item.dto.CommentPostDto;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.dto.ItemMapper;
import ru.practicum.shareit.server.item.dto.ItemPostDto;
import ru.practicum.shareit.server.item.model.Comment;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.model.ItemPager;
import ru.practicum.shareit.server.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.server.common.CommonUtils.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String MAX_SIZE = CommonUtils.INTEGER_MAX_VALUE;

    private final ItemService itemService;

    @PostMapping
    public ItemDto post(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @RequestBody ItemPostDto itemPostDto
    ) {
        Item item = toItem(itemPostDto);
        item.setOwnerId(userId);

        Item savedItem = itemService.save(item);
        return toItemDto(savedItem);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patch(
            @PathVariable long itemId,
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @RequestBody ItemPostDto itemPostDto
    ) {
        Item item = toItem(itemPostDto);
        item.setId(itemId);
        item.setOwnerId(userId);

        Item updatedItem = itemService.update(item);
        return toItemDto(updatedItem);
    }

    @GetMapping("/{itemId}")
    public ItemDto getByIdAndUserId(
            @PathVariable long itemId,
            @RequestHeader(X_SHARER_USER_ID) long userId
    ) {
        log.info("getByIdAndUserId(itemId = {}, userId = {})", itemId, userId);

        Item item = itemService.findByIdAndUserId(itemId, userId);
        return toItemDto(item);
    }

    @GetMapping
    public List<ItemDto> getAllByUserId(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = MAX_SIZE) int size,
            @RequestHeader(X_SHARER_USER_ID) long userId
    ) {
        log.info("getAllByUserId(from = {}, size = {}, userId = {})", from, size, userId);

        Pageable pageable = ItemPager.byIdAsc(from / size, size);
        List<Item> items = itemService.findAllByOwnerId(userId, pageable);
        return toItemsDto(items);
    }

    @GetMapping("/search")
    public List<ItemDto> getAllByNameOrDescriptionContaining(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = MAX_SIZE) int size,
            @RequestParam String text,
            @RequestHeader(X_SHARER_USER_ID) long userId
    ) {
        Pageable pageable = ItemPager.unsorted(from / size, size);
        List<Item> items = itemService.findAllByNameOrDescriptionContaining(userId, text, pageable);
        return toItemsDto(items);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(
            @PathVariable long itemId,
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @RequestBody CommentPostDto commentPostDto
    ) {
        Comment comment = toComment(commentPostDto);
        comment.getItem().setId(itemId);
        comment.getAuthor().setId(userId);

        Comment savedComment = itemService.save(comment);
        return toCommentDto(savedComment);
    }

    private Item toItem(ItemPostDto itemPostDto) {
        return ItemMapper.toItem(itemPostDto);
    }

    private Item toItem(ItemDto itemDto) {
        return ItemMapper.toItem(itemDto);
    }

    private ItemDto toItemDto(Item item) {
        return ItemMapper.toItemDto(item);
    }

    private List<ItemDto> toItemsDto(List<Item> items) {
        return ItemMapper.toItemsDto(items);
    }

    private CommentDto toCommentDto(Comment comment) {
        return CommentMapper.toCommentDto(comment);
    }

    private Comment toComment(CommentPostDto commentPostDto) {
        return CommentMapper.toComment(commentPostDto);
    }

}
