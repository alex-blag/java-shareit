package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Builder
@Getter
public class ItemRequest {

    private Long id;

    private String description;

    private User requester;

    private LocalDateTime created;

}
