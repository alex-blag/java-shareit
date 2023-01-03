package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemPager {

    public static Pageable unsorted(int page, int size) {
        return PageRequest.of(page, size, Sort.unsorted());
    }

    public static Pageable unsorted() {
        return unsorted(0, Integer.MAX_VALUE);
    }

}
