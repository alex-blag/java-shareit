package ru.practicum.shareit.booking.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingSortBy {

    public static final String START = "start";

    public static final Sort SORT_BY_START_DESC = Sort.by(Sort.Direction.DESC, BookingSortBy.START);

}
