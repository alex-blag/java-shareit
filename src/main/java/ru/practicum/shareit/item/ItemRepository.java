package ru.practicum.shareit.item;

import ru.practicum.shareit.common.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends Repository<Item> {

    List<Item> readAllByUserId(long userId);

    List<Item> findAllBySearchText(String text);

}
