package ru.practicum.shareit.server.exception;

public class BookingAlreadyApprovedException extends RuntimeException {

    public BookingAlreadyApprovedException(String message) {
        super(message);
    }

}
