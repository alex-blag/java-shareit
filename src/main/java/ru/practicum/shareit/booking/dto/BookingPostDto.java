package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.common.validation.NotNullAndFuture;
import ru.practicum.shareit.common.validation.NotNullAndFutureOrPresent;
import ru.practicum.shareit.common.validation.Post;
import ru.practicum.shareit.exception.ExceptionMessage;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingPostDto {

    @NotNullAndFutureOrPresent(groups = Post.class, message = ExceptionMessage.BOOKING_DATE_INVALID)
    private LocalDateTime start;

    @NotNullAndFuture(groups = Post.class, message = ExceptionMessage.BOOKING_DATE_INVALID)
    private LocalDateTime end;

    @NotNull(groups = Post.class)
    private Long itemId;

    @AssertTrue(groups = Post.class, message = ExceptionMessage.BOOKING_DATE_INVALID)
    private boolean isStartBeforeEnd() {
        return start.isBefore(end);
    }

}
