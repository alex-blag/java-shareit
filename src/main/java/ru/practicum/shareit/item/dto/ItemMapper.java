package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.BookingNearest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());

        itemDto.setOwnerId(item.getOwnerId());
        itemDto.setRequestId(item.getRequestId());

        BookingNearest lastBooking = item.getLastBooking();
        if (lastBooking != null) {
            itemDto.setLastBooking(BookingMapper.toBookingNearestDto(lastBooking));
        }

        BookingNearest nextBooking = item.getNextBooking();
        if (nextBooking != null) {
            itemDto.setNextBooking(BookingMapper.toBookingNearestDto(nextBooking));
        }

        List<Comment> comments = item.getComments() != null
                ? item.getComments()
                : List.of();

        List<CommentDto> commentsDto = CommentMapper.toCommentsDto(comments);
        itemDto.setComments(commentsDto);

        return itemDto;
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        if (itemDto != null) {
            item.setId(itemDto.getId());
            item.setName(itemDto.getName());
            item.setDescription(itemDto.getDescription());
            item.setAvailable(itemDto.getAvailable());
            item.setOwnerId(itemDto.getOwnerId());
            item.setRequestId(itemDto.getRequestId());
        }
        return item;
    }

    public static List<ItemDto> toItemsDto(List<Item> items) {
        if (items == null) {
            items = List.of();
        }

        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors
                        .toList());
    }

    public static Item toItem(ItemPostDto itemPostDto) {
        Item item = new Item();
        if (itemPostDto != null) {
            item.setName(itemPostDto.getName());
            item.setDescription(itemPostDto.getDescription());
            item.setAvailable(itemPostDto.getAvailable());
            item.setRequestId(itemPostDto.getRequestId());
        }
        return item;
    }

}
