package ru.practicum.shareit.gateway.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.gateway.common.validation.NotNullAndFuture;
import ru.practicum.shareit.gateway.common.validation.NotNullAndFutureOrPresent;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingPostDto {

    private static final String BOOKING_DATE_INVALID = "Booking date invalid";

    @NotNullAndFutureOrPresent(message = BOOKING_DATE_INVALID)
    private LocalDateTime start;

    @NotNullAndFuture(message = BOOKING_DATE_INVALID)
    private LocalDateTime end;

    @NotNull
    private Long itemId;

    @AssertTrue(message = BOOKING_DATE_INVALID)
    public boolean isStartBeforeEnd() {
        return start.isBefore(end);
    }

}
