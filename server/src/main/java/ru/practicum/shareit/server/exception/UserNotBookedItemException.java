package ru.practicum.shareit.server.exception;

public class UserNotBookedItemException extends RuntimeException {

    public UserNotBookedItemException(String message) {
        super(message);
    }

}
