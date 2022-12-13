package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentSort {

    public static final String CREATED = "created";

    public static final Sort BY_CREATED_DESC = Sort.by(CommentSort.CREATED).descending();

}
