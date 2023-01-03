package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(long bookerId, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatus(long bookerId, Status status, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartAfter(long bookerId, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(
            long bookerId,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    List<Booking> findAllByBookerIdAndEndBeforeAndStatus(
            long bookerId,
            LocalDateTime end,
            Status status,
            Pageable pageable
    );

    List<Booking> findAllByBookerIdAndItemIdAndEndBeforeAndStatus(
            long bookerId,
            long itemId,
            LocalDateTime end,
            Status status,
            Pageable pageable
    );

    List<Booking> findAllByItemIdIn(List<Long> itemIds, Pageable pageable);

    List<Booking> findAllByItemIdInAndStatus(List<Long> itemIds, Status status, Pageable pageable);

    List<Booking> findAllByItemIdInAndStartAfter(List<Long> itemIds, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByItemIdInAndStartBeforeAndEndAfter(
            List<Long> itemIds,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    List<Booking> findAllByItemIdInAndEndBeforeAndStatus(
            List<Long> itemIds,
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
