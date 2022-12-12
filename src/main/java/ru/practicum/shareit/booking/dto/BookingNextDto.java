package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingNextDto {

    private Long id;

    private Long bookerId;

    private LocalDateTime start;

    private LocalDateTime end;

}
