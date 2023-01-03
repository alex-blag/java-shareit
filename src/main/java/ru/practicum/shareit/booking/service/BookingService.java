package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.common.Service;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService extends Service<Booking> {

    Booking findByIdAndOwnerIdOrBookerId(long bookingId, long userId);

    List<Booking> findAllByBookerIdAndState(long bookerId, State state, Pageable pageable);

    List<Booking> findAllByOwnerIdAndState(long ownerId, State state, Pageable pageable);

    List<Booking> findAllByBookerIdAndItemIdAndEndBeforeAndStatus(
            long bookerId,
            long itemId,
            LocalDateTime end,
            Status status,
            Pageable pageable
    );

    List<Booking> findAllByItemIdInAndStartLessThanEqualAndStatus(
            List<Long> itemIds,
            LocalDateTime start,
            Status status,
            Pageable pageable
    );

    List<Booking> findAllByItemIdInAndStartAfterAndStatus(
            List<Long> itemIds,
            LocalDateTime start,
            Status status,
            Pageable pageable
    );

}
