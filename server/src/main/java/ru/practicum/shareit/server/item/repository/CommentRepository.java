package ru.practicum.shareit.server.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.server.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByItemId(long itemId);

    List<Comment> findAllByItemIdIn(List<Long> itemIds, Pageable pageable);

}
