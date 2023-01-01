package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.TestUtils;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotBookedItemException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemPager;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserService userService;

    @Mock
    BookingService bookingService;

    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    ItemServiceImpl itemServiceImpl;

    @Test
    void save_givenItem_expectCorrect() {
        Item item = TestUtils.getItem1();
        long itemOwnerId = item.getOwnerId();

        doNothing().when(userService).existsByIdOrThrow(itemOwnerId);
        when(itemRepository.save(item)).thenReturn(item);

        Item savedItem = itemServiceImpl.save(item);
        assertEquals(item, savedItem);
    }

    @Test
    void findAll() {
        assertThrows(UnsupportedOperationException.class, () -> itemServiceImpl.findAll());
    }

    @Test
    void findById_givenExistingItemId_expectCorrectItem() {
        Item item = TestUtils.getItem1();
        long itemId = item.getId();

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        Item foundItem = itemServiceImpl.findById(itemId);
        assertEquals(item, foundItem);
    }

    @Test
    void findById_givenNonExistingItemId_expectItemNotFoundException() {
        long itemId = 0L;

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemServiceImpl.findById(itemId));
    }

    @Test
    void update_givenItem_expectCorrect() {
        Item itemNameUpdate = TestUtils.getItem1NameUpdate();
        long itemId = itemNameUpdate.getId();
        long ownerId = itemNameUpdate.getOwnerId();

        doNothing().when(userService).existsByIdOrThrow(ownerId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(TestUtils.getItem1()));

        Item updatedItem = itemServiceImpl.update(itemNameUpdate);
        assertEquals(itemNameUpdate.getName(), updatedItem.getName());
    }

    @Test
    void deleteById() {
        assertThrows(UnsupportedOperationException.class, () -> itemServiceImpl.deleteById(0L));
    }

    @Test
    void findAllByOwnerId_givenOwnerId_expectCorrect() {
        long userId = TestUtils.USER1_ID;

        doNothing().when(userService).existsByIdOrThrow(userId);

        Item item = TestUtils.getItem1();
        List<Item> items = List.of(item);
        when(itemRepository.findAllByOwnerId(anyLong(), any(Pageable.class))).thenReturn(items);

        Booking lastBooking = TestUtils.getItem1LastBooking();
        List<Booking> lastBookings = List.of(lastBooking);
        when(bookingService.findAllByItemIdInAndStartLessThanEqualAndStatus(
                anyList(),
                any(LocalDateTime.class),
                any(Status.class),
                any(Pageable.class)
        )).thenReturn(lastBookings);

        Booking nextBooking = TestUtils.getItem1NextBooking();
        List<Booking> nextBookings = List.of(nextBooking);
        when(bookingService.findAllByItemIdInAndStartAfterAndStatus(
                anyList(),
                any(LocalDateTime.class),
                any(Status.class),
                any(Pageable.class)
        )).thenReturn(nextBookings);

        Comment comment = TestUtils.getItem1Comment();
        List<Comment> comments = List.of(comment);
        when(commentRepository.findAllByItemIdIn(anyList(), any(Pageable.class))).thenReturn(comments);

        List<Item> itemsByOwnerId = TestUtils.getItemsByOwnerId();
        List<Item> foundItems = itemServiceImpl.findAllByOwnerId(userId, ItemPager.unsorted());
        assertEquals(itemsByOwnerId, foundItems);
    }

    @Test
    void findAllByNameOrDescriptionContaining_givenExistingUserIdAndFilledText_expectCorrect() {
        long userId = TestUtils.USER1_ID;
        List<Item> items = List.of(TestUtils.getItem1());

        doNothing().when(userService).existsByIdOrThrow(userId);

        when(itemRepository.findAllByNameOrDescriptionContaining(anyString(), any(Pageable.class))).thenReturn(items);

        List<Item> foundItems = itemServiceImpl.findAllByNameOrDescriptionContaining(
                userId,
                "item",
                ItemPager.unsorted()
        );
        assertEquals(items, foundItems);
    }

    @Test
    void findByIdAndUserId_givenExistingItemIdAndUserId_expectCorrect() {
        Item item = TestUtils.getItem1();
        long itemId = item.getId();
        long userId = item.getOwnerId();
        List<Comment> comments = item.getComments();

        doNothing().when(userService).existsByIdOrThrow(userId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        Booking lastBooking = TestUtils.getItem1LastBooking();
        List<Booking> lastBookings = List.of(lastBooking);
        when(bookingService.findAllByItemIdInAndStartLessThanEqualAndStatus(
                anyList(),
                any(LocalDateTime.class),
                any(Status.class),
                any(Pageable.class)
        )).thenReturn(lastBookings);

        Booking nextBooking = TestUtils.getItem1NextBooking();
        List<Booking> nextBookings = List.of(nextBooking);
        when(bookingService.findAllByItemIdInAndStartAfterAndStatus(
                anyList(),
                any(LocalDateTime.class),
                any(Status.class),
                any(Pageable.class)
        )).thenReturn(nextBookings);

        when(commentRepository.findAllByItemId(itemId)).thenReturn(comments);

        Item foundItem = itemServiceImpl.findByIdAndUserId(itemId, userId);
        assertEquals(item, foundItem);
    }

    @Test
    void save_givenCommentAndExistingBookings_expectCorrect() {
        Comment comment = TestUtils.getItem1Comment();
        User author = comment.getAuthor();
        long authorId = author.getId();

        when(userService.findById(authorId)).thenReturn(author);

        Item item = comment.getItem();
        long itemId = item.getId();
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        Booking booking = TestUtils.getBooking1();
        List<Booking> bookings = List.of(booking);
        when(bookingService.findAllByBookerIdAndItemIdAndEndBeforeAndStatus(
                anyLong(),
                anyLong(),
                any(LocalDateTime.class),
                any(Status.class),
                any(Pageable.class)
        ))
                .thenReturn(bookings);

        when(commentRepository.save(comment)).thenReturn(comment);

        Comment savedComment = itemServiceImpl.save(comment);
        assertEquals(comment, savedComment);
    }

    @Test
    void save_givenCommentAndNonExistingBookings_expectUserNotBookedItemException() {
        Comment comment = TestUtils.getItem1Comment();
        User author = comment.getAuthor();
        long authorId = author.getId();

        when(userService.findById(authorId)).thenReturn(author);

        Item item = comment.getItem();
        long itemId = item.getId();
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        List<Booking> bookings = List.of();
        when(bookingService.findAllByBookerIdAndItemIdAndEndBeforeAndStatus(
                anyLong(),
                anyLong(),
                any(LocalDateTime.class),
                any(Status.class),
                any(Pageable.class)
        ))
                .thenReturn(bookings);

        assertThrows(UserNotBookedItemException.class, () -> itemServiceImpl.save(comment));
    }

    @Test
    void findAllByRequestIdIn_givenRequestItems_expectCorrect() {
        Item item1 = TestUtils.getItem1();
        List<Long> requestIds = List.of(item1.getRequestId());

        when(itemRepository.findAllByRequestIdIn(requestIds)).thenReturn(List.of(item1));

        List<Item> items = itemServiceImpl.findAllByRequestIdIn(requestIds);
        assertEquals(List.of(item1), items);
    }

}