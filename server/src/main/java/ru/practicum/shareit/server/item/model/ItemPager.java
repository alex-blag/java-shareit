package ru.practicum.shareit.server.item.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemPager {

    public static final String ID = "id";

    public static final Sort BY_ID_ASC = Sort.by(ID).ascending();

    public static Pageable unsorted(int page, int size) {
        return PageRequest.of(page, size, Sort.unsorted());
    }

    public static Pageable unsorted() {
        return unsorted(0, Integer.MAX_VALUE);
    }

    public static Pageable byIdAsc(int page, int size) {
        return PageRequest.of(page, size, BY_ID_ASC);
    }

}
