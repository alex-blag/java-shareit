package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserService userService;

    @Lazy
    private final BookingService bookingService;

    private final CommentRepository commentRepository;

    @Override
    public Item save(Item entity) {
        checkIfUserExistsById(entity.getOwnerId());
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

    @Override
    public Item update(Item entity) {
        long ownerId = entity.getOwnerId();
        checkIfUserExistsById(ownerId);

        long itemId = entity.getId();
        Item item = findById(itemId);

        checkUserOwnershipOfItem(ownerId, item);

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

        return itemRepository.save(item);
    }

    @Override
    public void deleteById(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Item> findAllByOwnerId(long ownerId) {
        checkIfUserExistsById(ownerId);

        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        items.forEach(item -> addBookingNext(ownerId, item));

        return items;
    }

    @Override
    public List<Item> findAllByNameOrDescriptionContaining(String text) {
        return itemRepository.findAllByNameOrDescriptionContaining(text);
    }

    @Override
    public Item findByIdAndUserId(long itemId, long userId) {
        checkIfUserExistsById(userId);
        Item item = findById(itemId);
        addBookingNext(userId, item);

        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        item.setComments(comments);

        return item;
    }

    @Override
    public Comment save(Comment comment) {
        long authorId = comment.getAuthor().getId();
        User author = userService.findById(authorId);
        comment.setAuthor(author);

        long itemId = comment.getItem().getId();
        Item item = findById(itemId);
        comment.setItem(item);

        List<Booking> bookings = bookingService.findAllByEndBeforeAndItemIdAndBookerIdAndStatusOrderByStartDesc(
                LocalDateTime.now(),
                itemId,
                authorId,
                Status.APPROVED
        );

        if (bookings.isEmpty()) {
            throw ExceptionUtils.getUserNotBookedItemException(authorId, itemId);
        }

        comment.setCreated(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    private Item addBookingNext(long userId, Item item) {
        List<Booking> bookings = bookingService.findAllByItemIdOrderByStartDesc(item.getId());

        BookingNext lastBooking = getLastBooking(bookings);
        if (lastBooking != null && userId != lastBooking.getBookerId()) {
            item.setNextBooking(lastBooking);
        }

        BookingNext nextBooking = getNextBooking(bookings);
        if (nextBooking != null && userId != nextBooking.getBookerId()) {
            item.setLastBooking(nextBooking);
        }

        return item;
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

    void checkIfUserExistsById(long userId) {
        userService.findById(userId);
    }

    private void checkUserOwnershipOfItem(long userId, Item item) {
        if (userId != item.getOwnerId()) {
            throw ExceptionUtils.getUserNotOwnItemException(userId, item.getId());
        }
    }

}
