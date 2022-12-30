package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.common.TestUtils;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@DataJpaTest
class ItemRepositoryTest {

    private static final String SEARCH_TEXT = "item";

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void findAllByNameOrDescriptionContaining_givenEmptyRepository_expectCorrect() {
        List<Item> items = itemRepository.findAllByNameOrDescriptionContaining(SEARCH_TEXT, Pageable.unpaged());
        Assertions.assertTrue(items.isEmpty());
    }

    @Test
    void findAllByNameOrDescriptionContaining_givenRepository_expectCorrect() {
        User user = TestUtils.getUserWithoutId();
        User savedUser = userRepository.save(user);

        Item item = TestUtils.getItemWithoutId();
        item.setOwnerId(savedUser.getId());
        Item savedItem = itemRepository.save(item);

        List<Item> items = itemRepository.findAllByNameOrDescriptionContaining(SEARCH_TEXT, Pageable.unpaged());
        Assertions.assertEquals(1, items.size());
        Assertions.assertEquals(savedItem.getId(), items.get(0).getId());
    }

}