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
import ru.practicum.shareit.common.Patch;
import ru.practicum.shareit.common.Post;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.common.CommonUtils.isStringNotBlank;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    private final ItemService itemService;

    @PostMapping
    public ItemDto post(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @Validated(Post.class) @RequestBody ItemDto itemDto
    ) {
        itemDto.setOwner(userId);
        Item added = itemService.add(toItem(itemDto));
        return toItemDto(added);
    }

    @GetMapping("/{itemId}")
    public ItemDto get(
            @PathVariable long itemId
    ) {
        Item found = itemService.find(itemId);
        return toItemDto(found);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patch(
            @PathVariable long itemId,
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @Validated(Patch.class) @RequestBody ItemDto itemDto
    ) {
        itemDto.setId(itemId);
        itemDto.setOwner(userId);
        Item changed = itemService.change(toItem(itemDto));
        return toItemDto(changed);
    }

    @GetMapping
    public List<ItemDto> getAllByUserId(
            @RequestHeader(X_SHARER_USER_ID) long userId
    ) {
        List<Item> found = itemService.findAllByUserId(userId);
        return toItemsDto(found);
    }

    @GetMapping("/search")
    public List<ItemDto> getAllBySearchText(
            @RequestParam String text
    ) {
        List<Item> found = findAllBySearchText(text);
        return toItemsDto(found);
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

    private List<Item> findAllBySearchText(String text) {
        return isStringNotBlank(text)
                ? itemService.findAllBySearchText(text)
                : List.of();
    }

}
