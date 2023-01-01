package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class BookingPostDtoTest {

    @Test
    void isStartBeforeEnd() {
        BookingPostDto b = new BookingPostDto(
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1L),
                1L
        );
        Assertions.assertTrue(b.isStartBeforeEnd());
    }

}
