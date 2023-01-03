package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(long ownerId, Pageable pageable);

    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.available IS TRUE " +
            "AND (" +
            "   LOWER(i.name) LIKE LOWER(CONCAT('%', ?1,'%')) " +
            "   OR LOWER(i.description) LIKE LOWER(CONCAT('%', ?1,'%')) " +
            ")")
    List<Item> findAllByNameOrDescriptionContaining(String text, Pageable pageable);

    List<Item> findAllByRequestIdIn(List<Long> requestIds);

}
