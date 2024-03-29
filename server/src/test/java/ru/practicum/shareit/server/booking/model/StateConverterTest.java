package ru.practicum.shareit.server.booking.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.server.exception.BookingStateUnknownException;
import ru.practicum.shareit.server.booking.model.State;
import ru.practicum.shareit.server.booking.model.StateConverter;

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
