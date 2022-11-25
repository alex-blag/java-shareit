package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryInMapImpl implements ItemRepository {

    private final Map<Long, Item> idToItem;

    private long id;

    public ItemRepositoryInMapImpl() {
        this.idToItem = new HashMap<>();
        this.id = 1;
    }

    @Override
    public Item create(Item obj) {
        long itemId = generateItemId();
        obj.setId(itemId);
        idToItem.put(itemId, obj);

        return idToItem.get(itemId);
    }

    @Override
    public List<Item> readAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Item> read(long id) {
        return Optional.ofNullable(idToItem.get(id));
    }

    @Override
    public Item update(Item obj) {
        long itemId = obj.getId();
        Item item = new Item(idToItem.get(itemId));

        String newName = obj.getName();
        if (newName != null) {
            item.setName(newName);
        }

        String newDescription = obj.getDescription();
        if (newDescription != null) {
            item.setDescription(newDescription);
        }

        Boolean newAvailable = obj.getAvailable();
        if (newAvailable != null) {
            item.setAvailable(newAvailable);
        }

        idToItem.put(itemId, item);

        return idToItem.get(itemId);
    }

    @Override
    public void destroy(long id) {
        throw new UnsupportedOperationException();
    }

    private long generateItemId() {
        return id++;
    }

    @Override
    public List<Item> readAllByUserId(long userId) {
        return idToItem.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .collect(Collectors
                        .toList());
    }

    @Override
    public List<Item> findAllBySearchText(String text) {
        List<Item> items;

        if (text.isBlank()) {
            items = List.of();
        } else {
            String lowerText = text.toLowerCase();
            items = idToItem.values().stream()
                    .filter(Item::getAvailable)
                    .filter(item -> isTextContainsInNameOrDescription(lowerText, item))
                    .collect(Collectors
                            .toList());
        }

        return items;
    }

    private boolean isTextContainsInNameOrDescription(String text, Item item) {
        String name = item.getName().toLowerCase();
        String description = item.getDescription().toLowerCase();
        return name.contains(text) || description.contains(text);
    }

}
