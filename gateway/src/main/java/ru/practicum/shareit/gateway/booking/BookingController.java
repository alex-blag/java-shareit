package ru.practicum.shareit.gateway.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.gateway.booking.dto.BookingPostDto;
import ru.practicum.shareit.gateway.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.gateway.common.CommonUtils.BOOKINGS_RESOURCE;
import static ru.practicum.shareit.gateway.common.CommonUtils.MAX_SIZE;
import static ru.practicum.shareit.gateway.common.CommonUtils.OWNER_RESOURCE;
import static ru.practicum.shareit.gateway.common.CommonUtils.X_SHARER_USER_ID;

@Slf4j
@Controller
@RequestMapping(path = BOOKINGS_RESOURCE)
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> post(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @RequestBody @Valid BookingPostDto bookingPostDto
    ) {
        log.info("post(userId = {}, {})", userId, bookingPostDto);

        return bookingClient.post(userId, bookingPostDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> patchByBookingId(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @PathVariable long bookingId,
            @RequestParam boolean approved
    ) {
        log.info("patchByBookingId(userId = {}, bookingId = {}, approved = {})", userId, bookingId, approved);

        return bookingClient.patchByBookingId(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getByIdAndOwnerIdOrBookerId(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @PathVariable long bookingId
    ) {
        log.info("getByIdAndOwnerIdOrBookerId(userId = {}, bookingId = {})", userId, bookingId);

        return bookingClient.getByIdAndOwnerIdOrBookerId(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByBookerIdAndState(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @RequestParam(required = false, defaultValue = BookingState.Default.ALL) String state,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = MAX_SIZE) @Positive int size
    ) {
        log.info("getAllByBookerIdAndState(userId = {}, state = {}, from = {}, size = {})",
                userId,
                state,
                from,
                size
        );

        return bookingClient.getAllByBookerIdAndState(userId, BookingState.getBookingStateOrThrow(state), from, size);
    }


    @GetMapping(OWNER_RESOURCE)
    public ResponseEntity<Object> getAllByOwnerIdAndState(
            @RequestHeader(X_SHARER_USER_ID) long userId,
            @RequestParam(required = false, defaultValue = BookingState.Default.ALL) String state,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = MAX_SIZE) @Positive int size
    ) {
        log.info("getAllByOwnerIdAndState(userId = {}, state = {}, from = {}, size = {})",
                userId,
                state,
                from,
                size
        );

        return bookingClient.getAllByOwnerIdAndState(
                OWNER_RESOURCE,
                userId,
                BookingState.getBookingStateOrThrow(state),
                from,
                size
        );
    }

    @ExceptionHandler
    public ResponseEntity<Object> handle(IllegalArgumentException e) {
        log.error(e.getMessage(), e);

        String errorJson = "{\"error\":\"" + e.getMessage() + "\"}";
        return new ResponseEntity<>(errorJson, HttpStatus.BAD_REQUEST);
    }

}
