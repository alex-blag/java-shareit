package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ExceptionUtils;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

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
                .orElseThrow(() -> ExceptionUtils.getItemNotFoundException(id));
    }

    @Override
    public Item change(Item obj) {
        long itemId = obj.getId();
        long userId = obj.getOwner();
        userService.find(userId);
        if (userId != find(itemId).getOwner()) {
            throw ExceptionUtils.getUserNotOwnItemException(userId, itemId);
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
