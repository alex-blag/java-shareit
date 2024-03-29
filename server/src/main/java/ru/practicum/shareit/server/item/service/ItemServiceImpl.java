package ru.practicum.shareit.server.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.BookingNearest;
import ru.practicum.shareit.server.booking.model.BookingPager;
import ru.practicum.shareit.server.booking.model.Status;
import ru.practicum.shareit.server.booking.service.BookingService;
import ru.practicum.shareit.server.common.CommonUtils;
import ru.practicum.shareit.server.exception.ExceptionUtils;
import ru.practicum.shareit.server.item.model.Comment;
import ru.practicum.shareit.server.item.model.CommentPager;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.repository.CommentRepository;
import ru.practicum.shareit.server.item.repository.ItemRepository;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

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
    public List<Item> findAllByOwnerId(long ownerId, Pageable pageable) {
        userExistsOrThrow(ownerId);

        List<Item> items = itemRepository.findAllByOwnerId(ownerId, pageable);
        List<Long> itemIds = items
                .stream()
                .map(Item::getId)
                .collect(toList());

        LocalDateTime now = LocalDateTime.now();

        Map<Item, List<Booking>> itemToLastBookings = bookingService.findAllByItemIdInAndStartLessThanEqualAndStatus(
                        itemIds,
                        now,
                        Status.APPROVED,
                        BookingPager.byStartDesc()
                )
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));

        Map<Item, List<Booking>> itemToNextBookings = bookingService.findAllByItemIdInAndStartAfterAndStatus(
                        itemIds,
                        now,
                        Status.APPROVED,
                        BookingPager.byStartAsc()
                )
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));

        Map<Item, List<Comment>> itemToComments = commentRepository
                .findAllByItemIdIn(itemIds, CommentPager.byCreatedDesc())
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));

        items.forEach(
                item -> {
                    List<Booking> lastBookings = itemToLastBookings.getOrDefault(item, List.of());
                    item.setLastBooking(getBookingNearest(ownerId, lastBookings));

                    List<Booking> nextBookings = itemToNextBookings.getOrDefault(item, List.of());
                    item.setNextBooking(getBookingNearest(ownerId, nextBookings));

                    item.setComments(itemToComments.getOrDefault(item, List.of()));
                }
        );

        return items;
    }

    @Override
    public List<Item> findAllByNameOrDescriptionContaining(long userId, String text, Pageable pageable) {
        userExistsOrThrow(userId);

        return itemRepository.findAllByNameOrDescriptionContaining(text, pageable);
    }

    @Override
    public Item findByIdAndUserId(long itemId, long userId) {
        userExistsOrThrow(userId);

        Item item = findById(itemId);

        LocalDateTime now = LocalDateTime.now();

        List<Booking> lastBookings = bookingService.findAllByItemIdInAndStartLessThanEqualAndStatus(
                List.of(item.getId()),
                now,
                Status.APPROVED,
                BookingPager.byStartDesc()
        );

        BookingNearest lastBooking = getBookingNearest(userId, lastBookings);
        item.setLastBooking(lastBooking);

        List<Booking> nextBookings = bookingService.findAllByItemIdInAndStartAfterAndStatus(
                List.of(item.getId()),
                now,
                Status.APPROVED,
                BookingPager.byStartAsc()
        );

        BookingNearest nextBooking = getBookingNearest(userId, nextBookings);
        item.setNextBooking(nextBooking);

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
                BookingPager.byStartDesc()
        );

        if (bookings.isEmpty()) {
            throw ExceptionUtils.getUserNotBookedItemException(authorId, itemId);
        }

        comment.setCreated(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public List<Item> findAllByRequestIdIn(List<Long> requestIds) {
        return itemRepository.findAllByRequestIdIn(requestIds);
    }

    private BookingNearest getBookingNearest(long userId, List<Booking> bookings) {
        if (bookings.isEmpty()) {
            return null;
        }

        Booking booking = bookings.get(0);
        if (userId == booking.getBooker().getId()) {
            return null;
        }

        return toBookingNearest(booking);
    }

    private BookingNearest toBookingNearest(Booking booking) {
        BookingNearest bookingNearest = null;
        if (booking != null) {
            bookingNearest = new BookingNearest();
            bookingNearest.setId(booking.getId());
            bookingNearest.setBookerId(booking.getBooker().getId());
            bookingNearest.setStart(booking.getStart());
            bookingNearest.setEnd(booking.getEnd());
        }
        return bookingNearest;
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
