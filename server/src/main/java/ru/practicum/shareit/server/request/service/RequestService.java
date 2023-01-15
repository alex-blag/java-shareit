package ru.practicum.shareit.server.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.server.common.Service;
import ru.practicum.shareit.server.request.model.Request;

import java.util.List;

public interface RequestService extends Service<Request> {

    List<Request> findAllByRequesterId(long requesterId, Pageable pageable);

    Request findByIdIfUserExists(long requestId, long userId);

    List<Request> findAllByUserId(long userId, Pageable pageable);

}
