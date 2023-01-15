package ru.practicum.shareit.server.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingNearestDto {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long bookerId;

}
