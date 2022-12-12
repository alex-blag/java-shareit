package ru.practicum.shareit.exception;

public class BookerOwnsItemException extends RuntimeException {

    public BookerOwnsItemException(String message) {
        super(message);
    }

}
