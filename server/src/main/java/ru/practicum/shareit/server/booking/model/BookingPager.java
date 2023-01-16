package ru.practicum.shareit.server.booking.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingPager {

    public static final String START = "start";

    public static final Sort BY_START_ASC = Sort.by(BookingPager.START).ascending();

    public static final Sort BY_START_DESC = Sort.by(BookingPager.START).descending();

    private static final int MAX_SIZE = Integer.MAX_VALUE;

    public static Pageable byStartAsc() {
        return byStartAsc(0, MAX_SIZE);
    }

    public static Pageable byStartAsc(int page, int size) {
        return PageRequest.of(page, size, BY_START_ASC);
    }

    public static Pageable byStartDesc() {
        return byStartDesc(0, MAX_SIZE);
    }

    public static Pageable byStartDesc(int page, int size) {
        return PageRequest.of(page, size, BY_START_DESC);
    }

}
