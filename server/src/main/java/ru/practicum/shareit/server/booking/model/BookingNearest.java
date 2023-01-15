package ru.practicum.shareit.server.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingNearest {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long bookerId;

}
