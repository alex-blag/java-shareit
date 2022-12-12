package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.common.CommonUtils.X_SHARER_USER_ID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto post(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @Validated(Post.class) @RequestBody ItemDto itemDto
    ) {
        itemDto.setOwner(userId);
        Item item = itemService.save(toItem(itemDto));
        return toItemDto(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patch(
            @PathVariable long itemId,
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @Validated(Patch.class) @RequestBody ItemDto itemDto
    ) {
        itemDto.setId(itemId);
        itemDto.setOwner(userId);
        Item item = itemService.update(toItem(itemDto));
        return toItemDto(item);
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
            @RequestHeader(X_SHARER_USER_ID) long userId
    ) {
        List<Item> items = itemService.findAllByOwnerId(userId);
        return toItemsDto(items);
    }

    @GetMapping("/search")
    public List<ItemDto> getAllByNameOrDescriptionContaining(
            @RequestParam String text
    ) {
        List<Item> items = findAllByNameOrDescriptionContaining(text);
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

    private List<Item> findAllByNameOrDescriptionContaining(String text) {
        return CommonUtils.isStringNotBlank(text)
                ? itemService.findAllByNameOrDescriptionContaining(text)
                : List.of();
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
