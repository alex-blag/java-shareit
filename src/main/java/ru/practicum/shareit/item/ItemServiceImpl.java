package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotOwnItemException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserService userService;

    @Override
    public Item add(Item obj) {
        userService.find(obj.getOwner());
        return itemRepository.create(obj);
    }

    @Override
    public List<Item> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item find(long id) {
        return itemRepository
                .read(id)
                .orElseThrow(ItemNotFoundException::new);
    }

    @Override
    public Item change(Item obj) {
        long itemId = obj.getId();
        long userId = obj.getOwner();
        userService.find(userId);
        if (userId != find(itemId).getOwner()) {
            throw new UserNotOwnItemException();
        }
        return itemRepository.update(obj);
    }

    @Override
    public void wipe(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Item> findAllByUserId(long userId) {
        userService.find(userId);
        return itemRepository.readAllByUserId(userId);
    }

    @Override
    public List<Item> findAllBySearchText(String text) {
        return itemRepository.findAllBySearchText(text);
    }

}
