package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.BookingStateUnknownException;

class StateConverterTest {

    private static final StateConverter STATE_CONVERTER = new StateConverter();

    @Test
    void convert_givenExistentState_expectCorrect() {
        State convertedState = STATE_CONVERTER.convert("ALL");
        Assertions.assertEquals(State.ALL, convertedState);
    }

    @Test
    void convert_givenNonExistentState_expectException() {
        Assertions.assertThrows(
                BookingStateUnknownException.class,
                () -> STATE_CONVERTER.convert("x")
        );
    }

}
