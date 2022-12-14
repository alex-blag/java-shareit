package ru.practicum.shareit.booking.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingSort {

    public static final String START = "start";

    public static final String END = "end";

    public static final Sort BY_START_ASC = Sort.by(BookingSort.START).ascending();

    public static final Sort BY_START_DESC = Sort.by(BookingSort.START).descending();

    public static final Sort BY_END_DESC = Sort.by(BookingSort.END).descending();

}
