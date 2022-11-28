package ru.practicum.shareit.common;

import java.util.List;

public interface Service<T> {

    T add(T obj);

    List<T> findAll();

    T find(long id);

    T change(T obj);

    void wipe(long id);

}
