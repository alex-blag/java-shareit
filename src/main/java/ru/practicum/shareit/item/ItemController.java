package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    private final ItemService itemService;

    @PostMapping
    public ItemDto postItem(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @Valid @RequestBody ItemDto itemDto
    ) {
        itemDto.setOwner(userId);
        Item added = itemService.add(toItem(itemDto));
        return toItemDto(added);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(
            @PathVariable long itemId
    ) {
        Item found = itemService.find(itemId);
        return toItemDto(found);
    }

    @GetMapping
    public List<ItemDto> getAllUserItems(
            @RequestHeader(X_SHARER_USER_ID) long userId
    ) {
        List<Item> found = itemService.findAllByUserId(userId);
        return toItemsDto(found);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearchText(
            @RequestParam String text
    ) {
        List<Item> items = itemService.findAllBySearchText(text);
        return toItemsDto(items);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(
            @PathVariable long itemId,
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @RequestBody ItemDto itemDto
    ) {
        itemDto.setId(itemId);
        itemDto.setOwner(userId);
        Item changed = itemService.change(toItem(itemDto));
        return toItemDto(changed);
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

}
