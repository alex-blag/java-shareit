package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(long bookerId, Sort sort);

    List<Booking> findAllByBookerIdAndStartAfter(long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findAllByBookerIdAndStatus(long bookerId, Status status, Sort sort);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(
            long bookerId,
            LocalDateTime start,
            LocalDateTime end,
            Sort sort
    );

    List<Booking> findAllByBookerIdAndEndBeforeAndStatus(long bookerId, LocalDateTime end, Status status, Sort sort);

    List<Booking> findAllByItemIdIn(List<Long> itemIds, Sort sort);

    List<Booking> findAllByItemIdInAndStartAfter(List<Long> itemIds, LocalDateTime start, Sort sort);

    List<Booking> findAllByItemIdInAndStatus(List<Long> itemIds, Status status, Sort sort);

    List<Booking> findAllByItemIdInAndStartBeforeAndEndAfter(
            List<Long> itemIds,
            LocalDateTime start,
            LocalDateTime end,
            Sort sort
    );

    List<Booking> findAllByItemIdInAndEndBeforeAndStatus(
            List<Long> itemIds,
            LocalDateTime end,
            Status status,
            Sort sort
    );

    List<Booking> findAllByBookerIdAndItemIdAndEndBeforeAndStatus(
            long bookerId,
            long itemId,
            LocalDateTime end,
            Status status,
            Sort sort
    );

}
