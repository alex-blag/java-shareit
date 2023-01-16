package ru.practicum.shareit.server.booking.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.Status;
import ru.practicum.shareit.server.common.TestUtils;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.repository.ItemRepository;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {

    private static final long BOOKING_DURATION_IN_MINUTES = 10L;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAllByBookerId_givenBookingsWithDifferentBookerIds_expectCorrect() {
        Booking booking1 = saveUniqueBooking();
        Booking booking2 = saveUniqueBooking();

        long booker1Id = booking1.getBooker().getId();

        List<Booking> foundBookings = bookingRepository.findAllByBookerId(
                booker1Id,
                Pageable.unpaged()
        );

        List<Booking> bookings = List.of(booking1);

        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByBookerIdAndStatus_givenBookingsWithDifferentBookerIdsAndStatusWaiting_expectCorrect() {
        Booking booking1 = saveUniqueBooking();
        Booking booking2 = saveUniqueBooking();

        long booker1Id = booking1.getBooker().getId();

        List<Booking> foundBookings = bookingRepository.findAllByBookerIdAndStatus(
                booker1Id,
                Status.WAITING,
                Pageable.unpaged()
        );

        List<Booking> bookings = List.of(booking1);

        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByBookerIdAndStartAfter_givenBookingsWithDifferentBookerIds_expectCorrect() {
        Booking booking1 = saveUniqueBooking();
        Booking booking2 = saveUniqueBooking();

        long booker1Id = booking1.getBooker().getId();

        List<Booking> foundBookings = bookingRepository.findAllByBookerIdAndStartAfter(
                booker1Id,
                LocalDateTime.now().minusMinutes(1L),
                Pageable.unpaged()
        );

        List<Booking> bookings = List.of(booking1);

        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndAfter_givenBookingsWithDifferentBookerIds_expectCorrect() {
        Booking booking1 = saveUniqueBooking();
        Booking booking2 = saveUniqueBooking();

        long booker1Id = booking1.getBooker().getId();
        LocalDateTime now = LocalDateTime.now();

        List<Booking> foundBookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(
                booker1Id,
                now.plusMinutes(1L),
                now.plusMinutes(BOOKING_DURATION_IN_MINUTES - 1),
                Pageable.unpaged()
        );

        List<Booking> bookings = List.of(booking1);

        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByBookerIdAndEndBeforeAndStatus_givenBookingsWithDifferentBookerIdsAndStatusWaiting_expectCorrect() {
        Booking booking1 = saveUniqueBooking();
        Booking booking2 = saveUniqueBooking();

        long booker1Id = booking1.getBooker().getId();

        List<Booking> foundBookings = bookingRepository.findAllByBookerIdAndEndBeforeAndStatus(
                booker1Id,
                LocalDateTime.now().plusMinutes(BOOKING_DURATION_IN_MINUTES + 1),
                Status.WAITING,
                Pageable.unpaged()
        );

        List<Booking> bookings = List.of(booking1);

        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByBookerIdAndItemIdAndEndBeforeAndStatus_givenBookingsWithDifferentBookerIdsAndItemIdsAndStatusWaiting_expectCorrect() {
        Booking booking1 = saveUniqueBooking();
        Booking booking2 = saveUniqueBooking();

        long booker1Id = booking1.getBooker().getId();
        long itemId = booking1.getItem().getId();

        List<Booking> foundBookings = bookingRepository.findAllByBookerIdAndItemIdAndEndBeforeAndStatus(
                booker1Id,
                itemId,
                LocalDateTime.now().plusMinutes(BOOKING_DURATION_IN_MINUTES + 1),
                Status.WAITING,
                Pageable.unpaged()
        );

        List<Booking> bookings = List.of(booking1);

        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByItemIdIn_givenBookingsWithDifferentItemIds_expectCorrect() {
        Booking booking1 = saveUniqueBooking();
        Booking booking2 = saveUniqueBooking();

        List<Booking> bookings = List.of(booking1, booking2);
        List<Long> itemIds = bookings
                .stream()
                .map(Booking::getItem)
                .map(Item::getId)
                .collect(Collectors
                        .toList());

        List<Booking> foundBookings = bookingRepository.findAllByItemIdIn(
                itemIds,
                Pageable.unpaged()
        );

        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByItemIdInAndStatus_givenBookingsWithDifferentItemIdsAndStatusWaiting_expectCorrect() {
        Booking booking1 = saveUniqueBooking();
        Booking booking2 = saveUniqueBooking();

        List<Booking> bookings = List.of(booking1, booking2);
        List<Long> itemIds = bookings
                .stream()
                .map(Booking::getItem)
                .map(Item::getId)
                .collect(Collectors
                        .toList());

        List<Booking> foundBookings = bookingRepository.findAllByItemIdInAndStatus(
                itemIds,
                Status.WAITING,
                Pageable.unpaged()
        );

        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByItemIdInAndStartAfter_givenBookingsWithDifferentItemIdsAndStarts_expectCorrect() {
        LocalDateTime now = LocalDateTime.now();
        Booking booking1 = saveUniqueBooking(now.plusMinutes(1L));
        Booking booking2 = saveUniqueBooking(now.minusMinutes(1L));

        List<Long> itemIds = Stream.of(booking1, booking2)
                .map(Booking::getItem)
                .map(Item::getId)
                .collect(Collectors
                        .toList());

        List<Booking> foundBookings = bookingRepository.findAllByItemIdInAndStartAfter(
                itemIds,
                now,
                Pageable.unpaged()
        );

        List<Booking> bookings = List.of(booking1);

        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByItemIdInAndStartBeforeAndEndAfter_givenBookingsWithDifferentItemIdsAndStartsAndEnds_expectCorrect() {
        LocalDateTime now = LocalDateTime.now();
        Booking booking1 = saveUniqueBooking(now.minusMinutes(5L));
        Booking booking2 = saveUniqueBooking(now.plusMinutes(BOOKING_DURATION_IN_MINUTES));

        List<Long> itemIds = Stream.of(booking1, booking2)
                .map(Booking::getItem)
                .map(Item::getId)
                .collect(Collectors
                        .toList());

        List<Booking> foundBookings = bookingRepository.findAllByItemIdInAndStartBeforeAndEndAfter(
                itemIds,
                now,
                now,
                Pageable.unpaged()
        );

        List<Booking> bookings = List.of(booking1);

        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByItemIdInAndEndBeforeAndStatus_givenBookingsWithDifferentItemIdsAndEndsAndStatusWaiting_expectCorrect() {
        LocalDateTime now = LocalDateTime.now();
        Booking booking1 = saveUniqueBooking(now.minusMinutes(BOOKING_DURATION_IN_MINUTES + 1));
        Booking booking2 = saveUniqueBooking();

        List<Long> itemIds = Stream.of(booking1, booking2)
                .map(Booking::getItem)
                .map(Item::getId)
                .collect(Collectors
                        .toList());

        List<Booking> foundBookings = bookingRepository.findAllByItemIdInAndEndBeforeAndStatus(
                itemIds,
                now,
                Status.WAITING,
                Pageable.unpaged()
        );

        List<Booking> bookings = List.of(booking1);

        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByItemIdInAndStartLessThanEqualAndStatus_givenBookingsWithDifferentItemIdsAndStartsAndStatusWaiting_expectCorrect() {
        LocalDateTime now = LocalDateTime.now();
        Booking booking1 = saveUniqueBooking(now.minusMinutes(1L));
        Booking booking2 = saveUniqueBooking(now.plusMinutes(1L));

        List<Long> itemIds = Stream.of(booking1, booking2)
                .map(Booking::getItem)
                .map(Item::getId)
                .collect(Collectors
                        .toList());

        List<Booking> foundBookings = bookingRepository.findAllByItemIdInAndStartLessThanEqualAndStatus(
                itemIds,
                now,
                Status.WAITING,
                Pageable.unpaged()
        );

        List<Booking> bookings = List.of(booking1);

        assertEquals(bookings, foundBookings);
    }

    @Test
    void findAllByItemIdInAndStartAfterAndStatus_givenBookingsWithDifferentItemIdsAndStartsAndStatusWaiting_expectCorrect() {
        LocalDateTime now = LocalDateTime.now();
        Booking booking1 = saveUniqueBooking(now.plusMinutes(1L));
        Booking booking2 = saveUniqueBooking(now.minusMinutes(1L));

        List<Long> itemIds = Stream.of(booking1, booking2)
                .map(Booking::getItem)
                .map(Item::getId)
                .collect(Collectors
                        .toList());

        List<Booking> foundBookings = bookingRepository.findAllByItemIdInAndStartAfterAndStatus(
                itemIds,
                now,
                Status.WAITING,
                Pageable.unpaged()
        );

        List<Booking> bookings = List.of(booking1);

        assertEquals(bookings, foundBookings);
    }

    private Booking saveUniqueBooking() {
        return saveUniqueBooking(LocalDateTime.now());
    }

    private Booking saveUniqueBooking(LocalDateTime start) {
        User owner = TestUtils.getUserWithoutId();
        owner = userRepository.save(owner);

        Item item = TestUtils.getItemWithoutIdAndOwnerId();
        item.setOwnerId(owner.getId());
        item = itemRepository.save(item);

        User booker = TestUtils.getUserWithoutId();
        booker = userRepository.save(booker);

        Booking booking = TestUtils.getBookingWithOnlyStatus();
        booking.setStart(start);
        booking.setEnd(start.plusMinutes(BOOKING_DURATION_IN_MINUTES));
        booking.setItem(item);
        booking.setBooker(booker);
        booking = bookingRepository.save(booking);
        return booking;
    }

}
