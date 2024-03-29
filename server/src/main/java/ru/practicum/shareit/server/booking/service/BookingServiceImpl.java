package ru.practicum.shareit.server.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.State;
import ru.practicum.shareit.server.booking.model.Status;
import ru.practicum.shareit.server.booking.repository.BookingRepository;
import ru.practicum.shareit.server.exception.ExceptionUtils;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.model.ItemPager;
import ru.practicum.shareit.server.item.service.ItemService;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final ItemService itemService;

    private final UserService userService;

    private final BookingRepository bookingRepository;

    @Transactional
    @Override
    public Booking save(Booking entity) {
        long bookerId = entity.getBooker().getId();
        User booker = userService.findById(bookerId);
        entity.setBooker(booker);

        long itemId = entity.getItem().getId();
        Item item = itemService.findById(itemId);
        bookerNotOwnsItemOrThrow(bookerId, item);
        itemAvailableOrThrow(item);
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

    @Transactional
    @Override
    public Booking update(Booking entity) {
        long ownerId = entity.getItem().getOwnerId();
        userExistsOrThrow(ownerId);

        Booking foundBooking = findById(entity.getId());
        bookingNotApprovedOrThrow(foundBooking);

        Item foundItem = foundBooking.getItem();
        userOwnsItemOrThrow(ownerId, foundItem);
        itemAvailableOrThrow(foundItem);

        foundBooking.setStatus(entity.getStatus());
        return foundBooking;
    }

    @Override
    public void deleteById(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Booking findByIdAndOwnerIdOrBookerId(long bookingId, long userId) {
        userExistsOrThrow(userId);

        Booking booking = findById(bookingId);
        userRelatedWithBookingOrThrow(userId, booking);

        return booking;
    }

    @Override
    public List<Booking> findAllByBookerIdAndState(long bookerId, State state, Pageable pageable) {
        userExistsOrThrow(bookerId);

        List<Booking> bookings = null;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerId(bookerId, pageable);
                break;

            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(
                        bookerId,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        pageable
                );
                break;

            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBeforeAndStatus(
                        bookerId,
                        LocalDateTime.now(),
                        Status.APPROVED,
                        pageable
                );
                break;

            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartAfter(
                        bookerId,
                        LocalDateTime.now(),
                        pageable
                );
                break;

            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, Status.WAITING, pageable);
                break;

            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, Status.REJECTED, pageable);
                break;
        }

        return bookings;
    }

    @Override
    public List<Booking> findAllByOwnerIdAndState(long ownerId, State state, Pageable pageable) {
        userExistsOrThrow(ownerId);

        List<Long> itemIds = itemService.findAllByOwnerId(ownerId, ItemPager.unsorted())
                .stream()
                .map(Item::getId)
                .collect(toList());

        List<Booking> bookings = null;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItemIdIn(itemIds, pageable);
                break;

            case CURRENT:
                bookings = bookingRepository.findAllByItemIdInAndStartBeforeAndEndAfter(
                        itemIds,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        pageable
                );
                break;

            case PAST:
                bookings = bookingRepository.findAllByItemIdInAndEndBeforeAndStatus(
                        itemIds,
                        LocalDateTime.now(),
                        Status.APPROVED,
                        pageable
                );
                break;

            case FUTURE:
                bookings = bookingRepository.findAllByItemIdInAndStartAfter(
                        itemIds,
                        LocalDateTime.now(),
                        pageable
                );
                break;

            case WAITING:
                bookings = bookingRepository.findAllByItemIdInAndStatus(itemIds, Status.WAITING, pageable);
                break;

            case REJECTED:
                bookings = bookingRepository.findAllByItemIdInAndStatus(itemIds, Status.REJECTED, pageable);
                break;
        }

        return bookings;
    }

    @Override
    public List<Booking> findAllByBookerIdAndItemIdAndEndBeforeAndStatus(
            long bookerId,
            long itemId,
            LocalDateTime end,
            Status status,
            Pageable pageable
    ) {
        return bookingRepository.findAllByBookerIdAndItemIdAndEndBeforeAndStatus(
                bookerId,
                itemId,
                end,
                status,
                pageable
        );
    }

    @Override
    public List<Booking> findAllByItemIdInAndStartLessThanEqualAndStatus(
            List<Long> itemIds,
            LocalDateTime start,
            Status status,
            Pageable pageable
    ) {
        return bookingRepository.findAllByItemIdInAndStartLessThanEqualAndStatus(itemIds, start, status, pageable);
    }

    @Override
    public List<Booking> findAllByItemIdInAndStartAfterAndStatus(
            List<Long> itemIds,
            LocalDateTime start,
            Status status,
            Pageable pageable
    ) {
        return bookingRepository.findAllByItemIdInAndStartAfterAndStatus(itemIds, start, status, pageable);
    }

    private void userExistsOrThrow(long userId) {
        userService.existsByIdOrThrow(userId);
    }

    private void userOwnsItemOrThrow(long userId, Item item) {
        if (userId != item.getOwnerId()) {
            throw ExceptionUtils.getUserNotOwnItemException(userId, item.getId());
        }
    }

    private void bookerNotOwnsItemOrThrow(long bookerId, Item item) {
        if (bookerId == item.getOwnerId()) {
            throw ExceptionUtils.getBookerOwnsItemException(bookerId, item.getId());
        }
    }

    private void itemAvailableOrThrow(Item item) {
        if (!item.getAvailable()) {
            throw ExceptionUtils.getItemNotAvailableException(item.getId());
        }
    }

    private void userRelatedWithBookingOrThrow(long userId, Booking booking) {
        boolean isOwner = userId == booking.getItem().getOwnerId();
        boolean isBooker = userId == booking.getBooker().getId();
        boolean isUserRelatedWithBooking = isOwner || isBooker;

        if (!isUserRelatedWithBooking) {
            throw ExceptionUtils.getUserNotRelatedWithBookingException(userId, booking.getId());
        }
    }

    private void bookingNotApprovedOrThrow(Booking booking) {
        if (booking.getStatus() == Status.APPROVED) {
            throw ExceptionUtils.getBookingAlreadyApprovedException(booking.getId());
        }
    }

}
