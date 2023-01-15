package ru.practicum.shareit.server.exception;

public class UserNotRelatedWithBookingException extends RuntimeException {

    public UserNotRelatedWithBookingException(String message) {
        super(message);
    }

}
