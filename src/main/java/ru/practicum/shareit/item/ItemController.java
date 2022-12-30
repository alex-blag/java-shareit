package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.common.CommonUtils;
import ru.practicum.shareit.common.validation.Patch;
import ru.practicum.shareit.common.validation.Post;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.CommentPostDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemPostDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemPager;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.common.CommonUtils.X_SHARER_USER_ID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private static final String MAX_SIZE = CommonUtils.INTEGER_MAX_VALUE;

    private final ItemService itemService;

    @PostMapping
    public ItemDto post(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @Validated(Post.class) @RequestBody ItemPostDto itemPostDto
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
            @Validated(Patch.class) @RequestBody ItemPostDto itemPostDto
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
        Item item = itemService.findByIdAndUserId(itemId, userId);
        return toItemDto(item);
    }

    @GetMapping
    public List<ItemDto> getAllByUserId(
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = MAX_SIZE) @Positive int size,
            @RequestHeader(X_SHARER_USER_ID) long userId
    ) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Item> items = itemService.findAllByOwnerId(userId, pageable);
        return toItemsDto(items);
    }

    @GetMapping("/search")
    public List<ItemDto> getAllByNameOrDescriptionContaining(
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = MAX_SIZE) @Positive int size,
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
            @Validated(Post.class) @RequestBody CommentPostDto commentPostDto
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
