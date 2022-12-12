package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(long bookerId);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long bookerId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long bookerId, Status status);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            long bookerId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Booking> findAllByBookerIdAndEndBeforeAndStatusOrderByStartDesc(
            long bookerId,
            LocalDateTime now,
            Status status
    );

    List<Booking> findAllByItemIdInOrderByStartDesc(List<Long> itemIds);

    List<Booking> findAllByItemIdInAndStartAfterOrderByStartDesc(List<Long> itemIds, LocalDateTime now);

    List<Booking> findAllByItemIdInAndStatusOrderByStartDesc(List<Long> itemIds, Status status);

    List<Booking> findAllByItemIdInAndStartBeforeAndEndAfterOrderByStartDesc(
            List<Long> itemIds,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Booking> findAllByItemIdInAndEndBeforeAndStatusOrderByStartDesc(
            List<Long> itemIds,
            LocalDateTime now,
            Status status
    );

    List<Booking> findAllByEndBeforeAndItemIdAndBookerIdAndStatusOrderByStartDesc(
            LocalDateTime now,
            long itemId,
            long bookerId,
            Status status
    );

}
