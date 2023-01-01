package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BookingStateUnknownExceptionTest {

    @Test
    void getState_expectCorrect() {
        BookingStateUnknownException e = new BookingStateUnknownException(null, "ALL");
        Assertions.assertEquals("ALL", e.getState());
    }

}