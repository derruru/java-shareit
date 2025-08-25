package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemStorageImpl implements ItemStorage {

    private final Map<Long, Item> items;

    private final UserService userService;

    public ItemStorageImpl(UserService userService) {
        this.items = new HashMap<>();
        this.userService = userService;
    }

    private long id = 1;

    @Override
    public Item createItem(long userId, Item item) {
        if (!userService.isExist(userId)) {
            throw new NotFoundException("Пользователя с таким id не существует!");
        }
        validCreatedItem(item);
        item.setOwner(userService.getUserById(userId));
        item.setId(id++);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(long userId, long itemId, Item item) {
        Item oldItem = this.getItemById(userId, itemId);
        if (item.getName() == null && item.getDescription() == null) {
            oldItem.setAvailable(item.getAvailable());
        } else if (item.getName() == null && item.getAvailable() == null) {
            oldItem.setDescription(item.getDescription());
        } else if (item.getAvailable() == null && item.getDescription() == null) {
            oldItem.setName(item.getName());
        } else {
            oldItem.setName(item.getName());
            oldItem.setDescription(item.getDescription());
            oldItem.setAvailable(item.getAvailable());
        }
        items.remove(itemId);
        items.put(oldItem.getId(), oldItem);
        return oldItem;
    }

    @Override
    public Item getItemById(long userId, long itemId) {
        if (!userService.isExist(userId)) {
            throw new NotFoundException("Пользователя с таким id не существует!");
        }
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Вещи с таким id не существует!");
        }
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllUserItems(long userId) {
        if (!userService.isExist(userId)) {
            throw new NotFoundException("Пользователя с таким id не существует!");
        }
        return new ArrayList<>(items.values()).stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String text) {
        String lowText = text.toLowerCase();
        if (lowText.isBlank()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(items.values().stream()
                .filter(item -> item.getDescription().toLowerCase().contains(lowText) ||
                        item.getName().toLowerCase().contains(lowText))
                .filter(item -> item.getAvailable() == true)
                .collect(Collectors.toList()));
    }

    private void validCreatedItem(Item item) {
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Не передано имя!");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Не передано описание!");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Не передан статус!");
        }
    }
}
