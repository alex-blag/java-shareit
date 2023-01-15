package ru.practicum.shareit.server.item.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentPager {

    public static final String CREATED = "created";

    public static final Sort BY_CREATED_DESC = Sort.by(CommentPager.CREATED).descending();

    private static final int MAX_SIZE = Integer.MAX_VALUE;

    public static Pageable byCreatedDesc() {
        return byCreatedDesc(0, MAX_SIZE);
    }

    private static Pageable byCreatedDesc(int page, int size) {
        return PageRequest.of(page, size, BY_CREATED_DESC);
    }

}
