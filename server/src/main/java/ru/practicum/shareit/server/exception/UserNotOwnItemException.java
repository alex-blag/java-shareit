package ru.practicum.shareit.server.exception;

public class UserNotOwnItemException extends RuntimeException {

    public UserNotOwnItemException(String message) {
        super(message);
    }

}
