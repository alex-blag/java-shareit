package ru.practicum.shareit.server.exception;

public class BookerOwnsItemException extends RuntimeException {

    public BookerOwnsItemException(String message) {
        super(message);
    }

}
