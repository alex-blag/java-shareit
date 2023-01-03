package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingNearestDto;
import ru.practicum.shareit.common.validation.Post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank(groups = Post.class)
    private String name;

    @NotBlank(groups = Post.class)
    private String description;

    @NotNull(groups = Post.class)
    private Boolean available;

    private Long ownerId;

    private Long requestId;

    private BookingNearestDto lastBooking;

    private BookingNearestDto nextBooking;

    private List<CommentDto> comments;

}
