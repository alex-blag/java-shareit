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

        itemDto.setOwner(item.getOwnerId());
        itemDto.setRequest(item.getRequestId());

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
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwnerId(itemDto.getOwner());
        item.setRequestId(itemDto.getRequest());
        return item;
    }

    public static List<ItemDto> toItemsDto(List<Item> items) {
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors
                        .toList());
    }

}
