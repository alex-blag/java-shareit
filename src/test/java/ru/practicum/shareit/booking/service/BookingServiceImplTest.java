package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingPager;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.TestUtils;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.UserNotRelatedWithBookingException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    UserService userService;

    @Mock
    ItemService itemService;

    @Mock
    BookingRepository bookingRepository;

    @InjectMocks
    BookingServiceImpl bookingServiceImpl;

    @Test
    void save_givenBooking_expectCorrect() {
        User user = TestUtils.getUser1();
        Item item = TestUtils.getItem1();
        Booking booking = TestUtils.getBooking1();

        when(userService.findById(anyLong())).thenReturn(user);
        when(itemService.findById(anyLong())).thenReturn(item);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking savedBooking = bookingServiceImpl.save(booking);
        assertEquals(booking, savedBooking);
    }

    @Test
    void findAll() {
        assertThrows(UnsupportedOperationException.class, () -> bookingServiceImpl.findAll());
    }

    @Test
    void findById_givenExistingBookingId_expectCorrectBooking() {
        Booking booking = TestUtils.getBooking1();
        long bookingId = booking.getId();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        Booking foundBooking = bookingServiceImpl.findById(bookingId);
        assertEquals(booking, foundBooking);
    }

    @Test
    void findById_givenNonExistingBookingId_expectBookingNotFoundException() {
        long bookingId = 0L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> bookingServiceImpl.findById(bookingId));
    }

    @Test
    void update_givenBookingStatusUpdate_expectCorrect() {
        Booking booking = TestUtils.getBooking1();
        Booking bookingStatusUpdate = TestUtils.getBooking1StatusUpdate();

        doNothing().when(userService).existsByIdOrThrow(anyLong());

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Booking updatedBooking = bookingServiceImpl.update(bookingStatusUpdate);
        assertEquals(bookingStatusUpdate.getStatus(), updatedBooking.getStatus());
    }

    @Test
    void deleteById() {
        assertThrows(UnsupportedOperationException.class, () -> bookingServiceImpl.deleteById(0L));
    }

    @Test
    void findByIdAndOwnerIdOrBookerId_givenUserRelatedWithBooking_expectCorrect() {
        Booking booking = TestUtils.getBooking1();
        long bookingId = booking.getId();
        long userId = booking.getBooker().getId();

        doNothing().when(userService).existsByIdOrThrow(anyLong());

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Booking foundBooking = bookingServiceImpl.findByIdAndOwnerIdOrBookerId(bookingId, userId);
        assertEquals(booking, foundBooking);
    }

    @Test
    void findByIdAndOwnerIdOrBookerId_givenUserNotRelatedWithBooking_expectUserNotRelatedWithBookingException() {
        Booking booking = TestUtils.getBooking1();
        long bookingId = booking.getId();

        doNothing().when(userService).existsByIdOrThrow(anyLong());

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(
                UserNotRelatedWithBookingException.class,
                () -> bookingServiceImpl.findByIdAndOwnerIdOrBookerId(bookingId, 0L)
        );
    }

    @Test
    void findAllByBookerIdAndState_givenBookerIdAndStateAll_expectCorrect() {
        Booking booking = TestUtils.getBooking1();
        long bookerId = booking.getBooker().getId();
        List<Booking> bookings = List.of(booking);

        doNothing().when(userService).existsByIdOrThrow(anyLong());

        when(bookingRepository.findAllByBookerId(anyLong(), any(Pageable.class))).thenReturn(bookings);

        List<Booking> foundBookings = bookingServiceImpl.findAllByBookerIdAndState(
                bookerId,
                State.ALL,
                BookingPager.byStartDesc()
        );
        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByBookerIdAndState_givenBookerIdAndStateCurrent_expectCorrect() {
        Booking booking = TestUtils.getBooking1();
        long bookerId = booking.getBooker().getId();
        List<Booking> bookings = List.of(booking);

        doNothing().when(userService).existsByIdOrThrow(anyLong());

        when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(
                anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class)
        )).thenReturn(bookings);

        List<Booking> foundBookings = bookingServiceImpl.findAllByBookerIdAndState(
                bookerId,
                State.CURRENT,
                BookingPager.byStartDesc()
        );
        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByBookerIdAndState_givenBookerIdAndStatePast_expectCorrect() {
        Booking booking = TestUtils.getBooking1();
        long bookerId = booking.getBooker().getId();
        List<Booking> bookings = List.of(booking);

        doNothing().when(userService).existsByIdOrThrow(anyLong());

        when(bookingRepository.findAllByBookerIdAndEndBeforeAndStatus(
                anyLong(),
                any(LocalDateTime.class),
                any(Status.class),
                any(Pageable.class)
        )).thenReturn(bookings);

        List<Booking> foundBookings = bookingServiceImpl.findAllByBookerIdAndState(
                bookerId,
                State.PAST,
                BookingPager.byStartDesc()
        );
        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByBookerIdAndState_givenBookerIdAndStateFuture_expectCorrect() {
        Booking booking = TestUtils.getBooking1();
        long bookerId = booking.getBooker().getId();
        List<Booking> bookings = List.of(booking);

        doNothing().when(userService).existsByIdOrThrow(anyLong());

        when(bookingRepository.findAllByBookerIdAndStartAfter(
                anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class)
        )).thenReturn(bookings);

        List<Booking> foundBookings = bookingServiceImpl.findAllByBookerIdAndState(
                bookerId,
                State.FUTURE,
                BookingPager.byStartDesc()
        );
        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByBookerIdAndState_givenBookerIdAndStateWaiting_expectCorrect() {
        Booking booking = TestUtils.getBooking1();
        long bookerId = booking.getBooker().getId();
        List<Booking> bookings = List.of(booking);

        doNothing().when(userService).existsByIdOrThrow(anyLong());

        when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(Status.class), any(Pageable.class)))
                .thenReturn(bookings);

        List<Booking> foundBookings = bookingServiceImpl.findAllByBookerIdAndState(
                bookerId,
                State.WAITING,
                BookingPager.byStartDesc()
        );
        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByBookerIdAndState_givenBookerIdAndStateRejected_expectCorrect() {
        Booking booking = TestUtils.getBooking1();
        long bookerId = booking.getBooker().getId();
        List<Booking> bookings = List.of(booking);

        doNothing().when(userService).existsByIdOrThrow(anyLong());

        when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(Status.class), any(Pageable.class)))
                .thenReturn(bookings);

        List<Booking> foundBookings = bookingServiceImpl.findAllByBookerIdAndState(
                bookerId,
                State.REJECTED,
                BookingPager.byStartDesc()
        );
        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByOwnerIdAndState_givenOwnerIdAndStateAll_expectCorrect() {
        Booking booking = TestUtils.getBooking1();
        List<Booking> bookings = List.of(booking);
        Item item = booking.getItem();
        List<Item> items = List.of(item);

        doNothing().when(userService).existsByIdOrThrow(anyLong());

        when(itemService.findAllByOwnerId(anyLong(), any(Pageable.class))).thenReturn(items);

        when(bookingRepository.findAllByItemIdIn(anyList(), any(Pageable.class))).thenReturn(bookings);

        List<Booking> foundBookings = bookingServiceImpl.findAllByOwnerIdAndState(
                item.getOwnerId(),
                State.ALL,
                BookingPager.byStartDesc()
        );
        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByOwnerIdAndState_givenOwnerIdAndStateCurrent_expectCorrect() {
        Booking booking = TestUtils.getBooking1();
        List<Booking> bookings = List.of(booking);
        Item item = booking.getItem();
        List<Item> items = List.of(item);

        doNothing().when(userService).existsByIdOrThrow(anyLong());

        when(itemService.findAllByOwnerId(anyLong(), any(Pageable.class))).thenReturn(items);

        when(bookingRepository.findAllByItemIdInAndStartBeforeAndEndAfter(
                anyList(),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class)
        )).thenReturn(bookings);

        List<Booking> foundBookings = bookingServiceImpl.findAllByOwnerIdAndState(
                item.getOwnerId(),
                State.CURRENT,
                BookingPager.byStartDesc()
        );
        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByOwnerIdAndState_givenOwnerIdAndStatePast_expectCorrect() {
        Booking booking = TestUtils.getBooking1();
        List<Booking> bookings = List.of(booking);
        Item item = booking.getItem();
        List<Item> items = List.of(item);

        doNothing().when(userService).existsByIdOrThrow(anyLong());

        when(itemService.findAllByOwnerId(anyLong(), any(Pageable.class))).thenReturn(items);

        when(bookingRepository.findAllByItemIdInAndEndBeforeAndStatus(
                anyList(),
                any(LocalDateTime.class),
                any(Status.class),
                any(Pageable.class)
        )).thenReturn(bookings);

        List<Booking> foundBookings = bookingServiceImpl.findAllByOwnerIdAndState(
                item.getOwnerId(),
                State.PAST,
                BookingPager.byStartDesc()
        );
        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByOwnerIdAndState_givenOwnerIdAndStateFuture_expectCorrect() {
        Booking booking = TestUtils.getBooking1();
        List<Booking> bookings = List.of(booking);
        Item item = booking.getItem();
        List<Item> items = List.of(item);

        doNothing().when(userService).existsByIdOrThrow(anyLong());

        when(itemService.findAllByOwnerId(anyLong(), any(Pageable.class))).thenReturn(items);

        when(bookingRepository.findAllByItemIdInAndStartAfter(
                anyList(),
                any(LocalDateTime.class),
                any(Pageable.class)
        )).thenReturn(bookings);

        List<Booking> foundBookings = bookingServiceImpl.findAllByOwnerIdAndState(
                item.getOwnerId(),
                State.FUTURE,
                BookingPager.byStartDesc()
        );
        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByOwnerIdAndState_givenOwnerIdAndStateWaiting_expectCorrect() {
        Booking booking = TestUtils.getBooking1();
        List<Booking> bookings = List.of(booking);
        Item item = booking.getItem();
        List<Item> items = List.of(item);

        doNothing().when(userService).existsByIdOrThrow(anyLong());

        when(itemService.findAllByOwnerId(anyLong(), any(Pageable.class))).thenReturn(items);

        when(bookingRepository.findAllByItemIdInAndStatus(
                anyList(),
                any(Status.class),
                any(Pageable.class)
        )).thenReturn(bookings);

        List<Booking> foundBookings = bookingServiceImpl.findAllByOwnerIdAndState(
                item.getOwnerId(),
                State.WAITING,
                BookingPager.byStartDesc()
        );
        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByOwnerIdAndState_givenOwnerIdAndStateRejected_expectCorrect() {
        Booking booking = TestUtils.getBooking1();
        List<Booking> bookings = List.of(booking);
        Item item = booking.getItem();
        List<Item> items = List.of(item);

        doNothing().when(userService).existsByIdOrThrow(anyLong());

        when(itemService.findAllByOwnerId(anyLong(), any(Pageable.class))).thenReturn(items);

        when(bookingRepository.findAllByItemIdInAndStatus(
                anyList(),
                any(Status.class),
                any(Pageable.class)
        )).thenReturn(bookings);

        List<Booking> foundBookings = bookingServiceImpl.findAllByOwnerIdAndState(
                item.getOwnerId(),
                State.REJECTED,
                BookingPager.byStartDesc()
        );
        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByBookerIdAndItemIdAndEndBeforeAndStatus_givenBookerIdAndItemIdAndEndAndStatus_expectCorrect() {
        Booking booking = TestUtils.getBooking1();
        long bookerId = booking.getBooker().getId();
        long itemId = booking.getItem().getId();
        List<Booking> bookings = List.of(booking);

        when(bookingRepository.findAllByBookerIdAndItemIdAndEndBeforeAndStatus(
                anyLong(),
                anyLong(),
                any(LocalDateTime.class),
                any(Status.class),
                any(Pageable.class)
        )).thenReturn(bookings);

        List<Booking> foundBooking = bookingServiceImpl.findAllByBookerIdAndItemIdAndEndBeforeAndStatus(
                bookerId,
                itemId,
                LocalDateTime.now(),
                Status.WAITING,
                BookingPager.byStartDesc()
        );

        assertEquals(bookings, foundBooking);
    }

    @Test
    void findAllByItemIdInAndStartLessThanEqualAndStatus_givenItemIdAndStartAndStatus_expectCorrect() {
        Booking booking = TestUtils.getBooking1();
        long bookingId = booking.getId();
        List<Booking> bookings = List.of(booking);

        when(bookingRepository.findAllByItemIdInAndStartLessThanEqualAndStatus(
                anyList(),
                any(LocalDateTime.class),
                any(Status.class),
                any(Pageable.class)
        )).thenReturn(bookings);

        List<Booking> foundBooking = bookingServiceImpl.findAllByItemIdInAndStartLessThanEqualAndStatus(
                List.of(bookingId),
                LocalDateTime.now(),
                Status.WAITING,
                BookingPager.byStartDesc()
        );

        assertEquals(bookings, foundBooking);
    }

    @Test
    void findAllByItemIdInAndStartAfterAndStatus_givenItemIdAndStartAndStatus_expectCorrect() {
        Booking booking = TestUtils.getBooking1();
        long bookingId = booking.getId();
        List<Booking> bookings = List.of(booking);

        when(bookingRepository.findAllByItemIdInAndStartAfterAndStatus(
                anyList(),
                any(LocalDateTime.class),
                any(Status.class),
                any(Pageable.class)
        )).thenReturn(bookings);

        List<Booking> foundBooking = bookingServiceImpl.findAllByItemIdInAndStartAfterAndStatus(
                List.of(bookingId),
                LocalDateTime.now(),
                Status.WAITING,
                BookingPager.byStartDesc()
        );

        assertEquals(bookings, foundBooking);
    }

}