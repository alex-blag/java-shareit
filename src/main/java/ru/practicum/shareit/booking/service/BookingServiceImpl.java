package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public List<Booking> findAllByBookerIdAndState(long bookerId, State state, Sort sort) {
        userExistsOrThrow(bookerId);

        List<Booking> bookings = null;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerId(bookerId, sort);
                break;

            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(
                        bookerId,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        sort
                );
                break;

            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBeforeAndStatus(
                        bookerId,
                        LocalDateTime.now(),
                        Status.APPROVED,
                        sort
                );
                break;

            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartAfter(
                        bookerId,
                        LocalDateTime.now(),
                        sort
                );
                break;

            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, Status.WAITING, sort);
                break;

            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, Status.REJECTED, sort);
                break;
        }

        return bookings;
    }

    @Override
    public List<Booking> findAllByOwnerIdAndState(long ownerId, State state, Sort sort) {
        userExistsOrThrow(ownerId);

        List<Long> itemIds = itemService.findAllByOwnerId(ownerId, sort).stream()
                .map(Item::getId)
                .collect(Collectors
                        .toList());

        List<Booking> bookings = null;
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItemIdIn(itemIds, sort);
                break;

            case CURRENT:
                bookings = bookingRepository.findAllByItemIdInAndStartBeforeAndEndAfter(
                        itemIds,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        sort
                );
                break;

            case PAST:
                bookings = bookingRepository.findAllByItemIdInAndEndBeforeAndStatus(
                        itemIds,
                        LocalDateTime.now(),
                        Status.APPROVED,
                        sort
                );
                break;

            case FUTURE:
                bookings = bookingRepository.findAllByItemIdInAndStartAfter(
                        itemIds,
                        LocalDateTime.now(),
                        sort
                );
                break;

            case WAITING:
                bookings = bookingRepository.findAllByItemIdInAndStatus(itemIds, Status.WAITING, sort);
                break;

            case REJECTED:
                bookings = bookingRepository.findAllByItemIdInAndStatus(itemIds, Status.REJECTED, sort);
                break;
        }

        return bookings;
    }

    @Override
    public List<Booking> findAllByItemIdIn(List<Long> itemIds, Sort sort) {
        return bookingRepository.findAllByItemIdIn(itemIds, sort);
    }

    @Override
    public List<Booking> findAllByBookerIdAndItemIdAndEndBeforeAndStatus(
            long bookerId,
            long itemId,
            LocalDateTime end,
            Status status,
            Sort sort
    ) {
        return bookingRepository.findAllByBookerIdAndItemIdAndEndBeforeAndStatus(
                bookerId,
                itemId,
                end,
                status,
                sort
        );
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
