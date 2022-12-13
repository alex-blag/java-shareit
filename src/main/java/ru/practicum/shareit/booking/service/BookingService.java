package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.common.Service;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService extends Service<Booking> {

    Booking findByIdAndOwnerIdOrBookerId(long bookingId, long userId);

    List<Booking> findAllByBookerIdAndState(long bookerId, State state, Sort sort);

    List<Booking> findAllByOwnerIdAndState(long ownerId, State state, Sort sort);

    List<Booking> findAllByItemIdIn(List<Long> itemIds, Sort sort);

    List<Booking> findAllByBookerIdAndItemIdAndEndBeforeAndStatus(
            long bookerId,
            long itemId,
            LocalDateTime end,
            Status status,
            Sort sort
    );

}
