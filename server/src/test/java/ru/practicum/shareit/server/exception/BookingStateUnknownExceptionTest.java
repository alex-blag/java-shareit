package ru.practicum.shareit.server.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.server.exception.BookingStateUnknownException;

class BookingStateUnknownExceptionTest {

    @Test
    void getState_expectCorrect() {
        BookingStateUnknownException e = new BookingStateUnknownException(null, "ALL");
        Assertions.assertEquals("ALL", e.getState());
    }

}