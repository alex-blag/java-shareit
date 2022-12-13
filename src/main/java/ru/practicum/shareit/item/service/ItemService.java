package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.common.Service;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService extends Service<Item> {

    List<Item> findAllByOwnerId(long ownerId, Sort sort);

    List<Item> findAllByNameOrDescriptionContaining(String text);

    Item findByIdAndUserId(long itemId, long userId);

    Comment save(Comment comment);

}
