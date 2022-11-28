package ru.practicum.shareit.exception;

public class UserNotOwnItemException extends RuntimeException {

    public UserNotOwnItemException(String message) {
        super(message);
    }

}
