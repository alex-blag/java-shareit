package ru.practicum.shareit.exception;

public class UserNotRelatedToBookingException extends RuntimeException {

    public UserNotRelatedToBookingException(String message) {
        super(message);
    }

}
