package ru.practicum.shareit.exception;

public class UserNotBookedItemException extends RuntimeException {

    public UserNotBookedItemException(String message) {
        super(message);
    }

}
