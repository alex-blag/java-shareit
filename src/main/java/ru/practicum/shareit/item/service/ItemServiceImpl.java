package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingNext;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.CommonUtils;
import ru.practicum.shareit.exception.ExceptionUtils;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.booking.model.BookingSortBy.SORT_BY_START_DESC;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserService userService;

    @Lazy
    private final BookingService bookingService;

    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Item save(Item entity) {
        userExistsOrThrow(entity.getOwnerId());
        return itemRepository.save(entity);
    }

    @Override
    public List<Item> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item findById(long id) {
        return itemRepository
                .findById(id)
                .orElseThrow(() -> ExceptionUtils.getItemNotFoundException(id));
    }

    @Transactional
    @Override
    public Item update(Item entity) {
        long ownerId = entity.getOwnerId();
        userExistsOrThrow(ownerId);

        long itemId = entity.getId();
        Item item = findById(itemId);

        userOwnsItemOrThrow(ownerId, item);

        String newName = entity.getName();
        if (CommonUtils.isStringNotBlank(newName)) {
            item.setName(newName);
        }

        String newDescription = entity.getDescription();
        if (CommonUtils.isStringNotBlank(newDescription)) {
            item.setDescription(newDescription);
        }

        Boolean newAvailable = entity.getAvailable();
        if (newAvailable != null) {
            item.setAvailable(newAvailable);
        }

        return item;
    }

    @Override
    public void deleteById(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Item> findAllByOwnerId(long ownerId, Sort sort) {
        userExistsOrThrow(ownerId);

        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        List<Long> itemIds = items
                .stream()
                .map(Item::getId)
                .collect(toList());

        Map<Item, List<Booking>> itemToBookings = bookingService
                .findAllByItemIdIn(itemIds, sort)
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));

        items.forEach(
                item -> appendLastAndNextBookingToItem(ownerId, item, itemToBookings.getOrDefault(item, List.of()))
        );

        return items;
    }

    @Override
    public List<Item> findAllByNameOrDescriptionContaining(String text) {
        return itemRepository.findAllByNameOrDescriptionContaining(text);
    }

    @Override
    public Item findByIdAndUserId(long itemId, long userId) {
        userExistsOrThrow(userId);
        Item item = findById(itemId);

        List<Booking> bookings = bookingService.findAllByItemIdIn(List.of(item.getId()), SORT_BY_START_DESC);
        appendLastAndNextBookingToItem(userId, item, bookings);

        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        item.setComments(comments);

        return item;
    }

    @Transactional
    @Override
    public Comment save(Comment comment) {
        long authorId = comment.getAuthor().getId();
        User author = userService.findById(authorId);
        comment.setAuthor(author);

        long itemId = comment.getItem().getId();
        Item item = findById(itemId);
        comment.setItem(item);

        List<Booking> bookings = bookingService.findAllByBookerIdAndItemIdAndEndBeforeAndStatus(
                authorId,
                itemId,
                LocalDateTime.now(),
                Status.APPROVED,
                SORT_BY_START_DESC
        );

        if (bookings.isEmpty()) {
            throw ExceptionUtils.getUserNotBookedItemException(authorId, itemId);
        }

        comment.setCreated(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    private void appendLastAndNextBookingToItem(long userId, Item item, List<Booking> bookings) {
        BookingNext lastBooking = getLastBooking(bookings);
        if (lastBooking != null && userId != lastBooking.getBookerId()) {
            item.setNextBooking(lastBooking);
        }

        BookingNext nextBooking = getNextBooking(bookings);
        if (nextBooking != null && userId != nextBooking.getBookerId()) {
            item.setLastBooking(nextBooking);
        }
    }

    private BookingNext getLastBooking(List<Booking> bookings) {
        return buildBookingNext(bookings, 1);
    }

    private BookingNext getNextBooking(List<Booking> bookings) {
        return buildBookingNext(bookings, 2);
    }

    private BookingNext buildBookingNext(List<Booking> bookings, int bookingNumber) {
        BookingNext bookingNext = null;

        if (bookings.size() >= bookingNumber) {
            Booking booking = bookings.get(bookingNumber - 1);

            bookingNext = new BookingNext();
            bookingNext.setId(booking.getId());
            bookingNext.setBookerId(booking.getBooker().getId());
            bookingNext.setStart(booking.getStart());
            bookingNext.setEnd(booking.getEnd());
        }

        return bookingNext;
    }

    private void userExistsOrThrow(long userId) {
        userService.existsByIdOrThrow(userId);
    }

    private void userOwnsItemOrThrow(long userId, Item item) {
        if (userId != item.getOwnerId()) {
            throw ExceptionUtils.getUserNotOwnItemException(userId, item.getId());
        }
    }

}
