package ru.practicum.shareit.server.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.dto.BookingMapper;
import ru.practicum.shareit.server.booking.dto.BookingPostDto;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.BookingPager;
import ru.practicum.shareit.server.booking.model.State;
import ru.practicum.shareit.server.booking.model.Status;
import ru.practicum.shareit.server.booking.service.BookingService;
import ru.practicum.shareit.server.common.CommonUtils;

import java.util.List;

import static ru.practicum.shareit.server.common.CommonUtils.X_SHARER_USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    public static final String MAX_SIZE = CommonUtils.INTEGER_MAX_VALUE;

    private final BookingService bookingService;

    @PostMapping
    public BookingDto post(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @RequestBody BookingPostDto bookingPostDto
    ) {
        Booking booking = toBooking(bookingPostDto);
        booking.getBooker().setId(userId);
        booking.setStatus(Status.WAITING);

        Booking savedBooking = bookingService.save(booking);
        return toBookingDto(savedBooking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto patch(
            @PathVariable long bookingId,
            @RequestParam boolean approved,
            @RequestHeader(X_SHARER_USER_ID) long userId
    ) {
        Booking booking = toBooking(new BookingDto());
        booking.setId(bookingId);
        booking.getItem().setOwnerId(userId);

        Status status = approved
                ? Status.APPROVED
                : Status.REJECTED;
        booking.setStatus(status);

        Booking updatedBooking = bookingService.update(booking);
        return toBookingDto(updatedBooking);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getByIdAndOwnerIdOrBookerId(
            @PathVariable long bookingId,
            @RequestHeader(X_SHARER_USER_ID) long userId
    ) {
        Booking foundBooking = bookingService.findByIdAndOwnerIdOrBookerId(bookingId, userId);
        return toBookingDto(foundBooking);
    }

    @GetMapping
    public List<BookingDto> getAllByBookerIdAndState(
            @RequestParam(required = false, defaultValue = State.Default.ALL) State state,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = MAX_SIZE) int size,
            @RequestHeader(X_SHARER_USER_ID) long userId
    ) {
        Pageable pageable = BookingPager.byStartDesc(from / size, size);
        List<Booking> bookings = bookingService.findAllByBookerIdAndState(userId, state, pageable);
        return toBookingsDto(bookings);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwnerIdAndState(
            @RequestParam(required = false, defaultValue = State.Default.ALL) State state,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = MAX_SIZE) int size,
            @RequestHeader(X_SHARER_USER_ID) long userId
    ) {
        Pageable pageable = BookingPager.byStartDesc(from / size, size);
        List<Booking> bookings = bookingService.findAllByOwnerIdAndState(userId, state, pageable);
        return toBookingsDto(bookings);
    }

    private Booking toBooking(BookingPostDto dto) {
        return BookingMapper.toBooking(dto);
    }

    private Booking toBooking(BookingDto bookingDto) {
        return BookingMapper.toBooking(bookingDto);
    }

    private BookingDto toBookingDto(Booking booking) {
        return BookingMapper.toBookingDto(booking);
    }

    private List<BookingDto> toBookingsDto(List<Booking> bookings) {
        return BookingMapper.toBookingsDto(bookings);
    }

}
