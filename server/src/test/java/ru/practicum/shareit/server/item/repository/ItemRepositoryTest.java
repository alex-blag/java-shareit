package ru.practicum.shareit.server.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.server.common.TestUtils;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.repository.UserRepository;

import java.util.List;

@DataJpaTest
class ItemRepositoryTest {

    private static final String SEARCH_TEXT = "item";

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAllByNameOrDescriptionContaining_givenEmptyRepository_expectCorrect() {
        List<Item> items = itemRepository.findAllByNameOrDescriptionContaining(SEARCH_TEXT, Pageable.unpaged());
        Assertions.assertTrue(items.isEmpty());
    }

    @Test
    void findAllByNameOrDescriptionContaining_givenRepository_expectCorrect() {
        User user = TestUtils.getUserWithoutId();
        User savedUser = userRepository.save(user);

        Item item = TestUtils.getItemWithoutIdAndOwnerId();
        item.setOwnerId(savedUser.getId());
        Item savedItem = itemRepository.save(item);

        List<Item> items = itemRepository.findAllByNameOrDescriptionContaining(SEARCH_TEXT, Pageable.unpaged());
        Assertions.assertEquals(1, items.size());
        Assertions.assertEquals(savedItem.getId(), items.get(0).getId());
    }

}