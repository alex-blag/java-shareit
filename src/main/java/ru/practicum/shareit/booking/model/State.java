package ru.practicum.shareit.booking.model;

public enum State {

    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static class Default {
        public static final String ALL = "ALL";
    }

}
