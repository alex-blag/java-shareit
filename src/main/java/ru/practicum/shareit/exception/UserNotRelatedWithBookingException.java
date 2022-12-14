package ru.practicum.shareit.exception;

public class UserNotRelatedWithBookingException extends RuntimeException {

    public UserNotRelatedWithBookingException(String message) {
        super(message);
    }

}
