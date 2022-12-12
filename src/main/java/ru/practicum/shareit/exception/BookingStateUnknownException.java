package ru.practicum.shareit.exception;

public class BookingStateUnknownException extends RuntimeException {

    private final String state;

    public BookingStateUnknownException(String message, String state) {
        super(message);
        this.state = state;
    }

    public String getState() {
        return state;
    }

}
