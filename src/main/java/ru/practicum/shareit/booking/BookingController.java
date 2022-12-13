package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingPostDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.validation.Post;

import java.util.List;

import static ru.practicum.shareit.booking.model.BookingSortBy.SORT_BY_START_DESC;
import static ru.practicum.shareit.common.CommonUtils.X_SHARER_USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto post(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @Validated(Post.class) @RequestBody BookingPostDto bookingPostDto
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
            @RequestHeader(X_SHARER_USER_ID) long userId
    ) {
        List<Booking> bookings = bookingService.findAllByBookerIdAndState(userId, state, SORT_BY_START_DESC);
        return toBookingsDto(bookings);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwnerIdAndState(
            @RequestParam(required = false, defaultValue = State.Default.ALL) State state,
            @RequestHeader(X_SHARER_USER_ID) long userId
    ) {
        List<Booking> bookings = bookingService.findAllByOwnerIdAndState(userId, state, SORT_BY_START_DESC);
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
