package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ExceptionUtils;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final ItemService itemService;

    private final UserService userService;

    private final BookingRepository bookingRepository;

    @Override
    public Booking save(Booking entity) {
        long bookerId = entity.getBooker().getId();
        User booker = userService.findById(bookerId);
        entity.setBooker(booker);

        long itemId = entity.getItem().getId();
        Item item = itemService.findById(itemId);
        checkIfBookerOwnsItem(bookerId, item);
        checkIfItemNotAvailable(item);
        entity.setItem(item);

        return bookingRepository.save(entity);
    }

    @Override
    public List<Booking> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Booking findById(long id) {
        return bookingRepository
                .findById(id)
                .orElseThrow(() -> ExceptionUtils.getBookingNotFoundException(id));
    }

    @Override
    public Booking update(Booking entity) {
        long ownerId = entity.getItem().getOwnerId();
        checkIfUserExistsById(ownerId);

        Booking foundBooking = findById(entity.getId());
        checkIfBookingAlreadyApproved(foundBooking);

        Item foundItem = foundBooking.getItem();
        checkIfUserNotOwnsItem(ownerId, foundItem);
        checkIfItemNotAvailable(foundItem);

        foundBooking.setStatus(entity.getStatus());
        return bookingRepository.save(foundBooking);
    }

    @Override
    public void deleteById(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Booking findByIdAndOwnerIdOrBookerId(long bookingId, long userId) {
        checkIfUserExistsById(userId);

        Booking booking = findById(bookingId);
        checkIfUserNotRelatedToBooking(userId, booking);

        return booking;
    }

    @Override
    public List<Booking> findAllByBookerIdAndStateOrderByDateDesc(long bookerId, State state) {
        checkIfUserExistsById(bookerId);

        List<Booking> bookings = null;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId);
                break;

            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        bookerId,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );
                break;

            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBeforeAndStatusOrderByStartDesc(
                        bookerId,
                        LocalDateTime.now(),
                        Status.APPROVED
                );
                break;

            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now());
                break;

            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId, Status.WAITING);
                break;

            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId, Status.REJECTED);
                break;
        }

        return bookings;
    }

    @Override
    public List<Booking> findAllByOwnerIdAndStateOrderByDateDesc(long ownerId, State state) {
        checkIfUserExistsById(ownerId);

        List<Long> itemIds = itemService.findAllByOwnerId(ownerId).stream()
                .map(Item::getId)
                .collect(Collectors
                        .toList());

        List<Booking> bookings = null;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItemIdInOrderByStartDesc(itemIds);
                break;

            case CURRENT:
                bookings = bookingRepository.findAllByItemIdInAndStartBeforeAndEndAfterOrderByStartDesc(
                        itemIds,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );
                break;

            case PAST:
                bookings = bookingRepository.findAllByItemIdInAndEndBeforeAndStatusOrderByStartDesc(
                        itemIds,
                        LocalDateTime.now(),
                        Status.APPROVED
                );
                break;

            case FUTURE:
                bookings = bookingRepository.findAllByItemIdInAndStartAfterOrderByStartDesc(itemIds, LocalDateTime.now());
                break;

            case WAITING:
                bookings = bookingRepository.findAllByItemIdInAndStatusOrderByStartDesc(itemIds, Status.WAITING);
                break;

            case REJECTED:
                bookings = bookingRepository.findAllByItemIdInAndStatusOrderByStartDesc(itemIds, Status.REJECTED);
                break;
        }

        return bookings;
    }

    @Override
    public List<Booking> findAllByItemIdOrderByStartDesc(long itemId) {
        return bookingRepository.findAllByItemIdInOrderByStartDesc(List.of(itemId));
    }

    @Override
    public List<Booking> findAllByEndBeforeAndItemIdAndBookerIdAndStatusOrderByStartDesc(
            LocalDateTime now,
            long itemId,
            long bookerId,
            Status status
    ) {
        return bookingRepository.findAllByEndBeforeAndItemIdAndBookerIdAndStatusOrderByStartDesc(
                now,
                itemId,
                bookerId,
                status
        );
    }

    void checkIfUserExistsById(long userId) {
        userService.findById(userId);
    }

    private void checkIfUserNotOwnsItem(long userId, Item item) {
        if (userId != item.getOwnerId()) {
            throw ExceptionUtils.getUserNotOwnItemException(userId, item.getId());
        }
    }

    private void checkIfBookerOwnsItem(long bookerId, Item item) {
        if (bookerId == item.getOwnerId()) {
            throw ExceptionUtils.getBookerOwnsItemException(bookerId, item.getId());
        }
    }

    private void checkIfItemNotAvailable(Item item) {
        if (!item.getAvailable()) {
            throw ExceptionUtils.getItemNotAvailableException(item.getId());
        }
    }

    private void checkIfUserNotRelatedToBooking(long userId, Booking booking) {
        boolean isOwner = userId == booking.getItem().getOwnerId();
        boolean isBooker = userId == booking.getBooker().getId();
        boolean isUserRelatedToBooking = isOwner || isBooker;

        if (!isUserRelatedToBooking) {
            throw ExceptionUtils.getUserNotRelatedToBookingException(userId, booking.getId());
        }
    }

    private void checkIfBookingAlreadyApproved(Booking booking) {
        if (booking.getStatus() == Status.APPROVED) {
            throw ExceptionUtils.getBookingAlreadyApprovedException(booking.getId());
        }
    }

}
