package ru.practicum.shareit.server.common;

import java.util.List;

public interface Service<T> {

    T save(T entity);

    List<T> findAll();

    T findById(long id);

    T update(T entity);

    void deleteById(long id);

}
