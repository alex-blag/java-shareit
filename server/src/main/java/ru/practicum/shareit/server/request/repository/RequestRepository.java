package ru.practicum.shareit.server.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.server.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(long requesterId, Pageable pageable);

    List<Request> findAllByRequesterIdNot(long userId, Pageable pageable);

}
