package ru.practicum.shareit.server.user.service;

import ru.practicum.shareit.server.common.Service;
import ru.practicum.shareit.server.user.model.User;

public interface UserService extends Service<User> {

    void existsByIdOrThrow(long id);

}
