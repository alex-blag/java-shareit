package ru.practicum.shareit.common;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    T create(T obj);

    List<T> readAll();

    Optional<T> read(long id);

    T update(T obj);

    void destroy(long id);

}
