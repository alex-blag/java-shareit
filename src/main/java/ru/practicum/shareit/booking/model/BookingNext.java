package ru.practicum.shareit.booking.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingNext {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long bookerId;

}
