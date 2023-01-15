package ru.practicum.shareit.server.item.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.server.common.Service;
import ru.practicum.shareit.server.item.model.Comment;
import ru.practicum.shareit.server.item.model.Item;

import java.util.List;

public interface ItemService extends Service<Item> {

    List<Item> findAllByOwnerId(long ownerId, Pageable pageable);

    List<Item> findAllByNameOrDescriptionContaining(long userId, String text, Pageable pageable);

    Item findByIdAndUserId(long itemId, long userId);

    Comment save(Comment comment);

    List<Item> findAllByRequestIdIn(List<Long> requestIds);

}
