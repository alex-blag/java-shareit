package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingNearestDto;
import ru.practicum.shareit.common.validation.Post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ItemDto {

    private Long id;

    @NotBlank(groups = Post.class)
    private String name;

    @NotBlank(groups = Post.class)
    private String description;

    @NotNull(groups = Post.class)
    private Boolean available;

    private Long owner;

    private Long request;

    private BookingNearestDto lastBooking;

    private BookingNearestDto nextBooking;

    private List<CommentDto> comments;

}
