package ru.practicum.shareit.gateway.booking.dto;

import java.util.Optional;

public enum BookingState {

    ALL,
    CURRENT,
    FUTURE,
    PAST,
    REJECTED,
    WAITING;

    public static Optional<BookingState> from(String stringState) {
        for (BookingState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }

    public static BookingState getBookingStateOrThrow(String state) {
        return BookingState
                .from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
    }

    public static class Default {
        public static final String ALL = "ALL";
    }

}
