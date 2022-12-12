package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.common.Service;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService extends Service<Booking> {

    Booking findByIdAndOwnerIdOrBookerId(long bookingId, long userId);

    List<Booking> findAllByBookerIdAndStateOrderByDateDesc(long bookerId, State state);

    List<Booking> findAllByOwnerIdAndStateOrderByDateDesc(long ownerId, State state);

    List<Booking> findAllByItemIdOrderByStartDesc(long itemId);

    List<Booking> findAllByEndBeforeAndItemIdAndBookerIdAndStatusOrderByStartDesc(
            LocalDateTime now,
            long itemId,
            long bookerId,
            Status status
    );

}
