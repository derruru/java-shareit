package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component
public class ItemRepositoryImpl implements ItemRepository {

    private Map<Long, Item> items;

    private UserService userService;

    public ItemRepositoryImpl(UserService userService) {
        items = new HashMap<>();
        this.userService = userService;
    }

    @Override
    public Item createItem(ItemDto itemDto, long userId) {
        if (userService.getUserById(userId) == null) {
            throw new NotFoundException("Пользователя с таким id не существует!");
        }
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new ValidateException("Не передано имя!");
        }
        if (itemDto.getDescription() == null) {
            throw new ValidateException("Не передано описание!");
        }
        if (itemDto.getAvailable() == null) {
            throw new ValidateException("Не передан статус!");
        }

        Item item = ItemMapper.fromDto(itemDto);
        item.setOwner(userService.getUserById(userId));

        items.put(item.getId(), item);

        return item;
    }

    @Override
    public Item updateItem(ItemDto itemDto, long userId, long itemId) {
        if (userService.getUserById(userId) == null) {
            throw new NotFoundException("Пользователя с таким id не существует!");
        }
        Item item = getItemById(itemId);
        if (itemDto.getAvailable() == null && itemDto.getName() == null) {
            item.setDescription(itemDto.getDescription());
        } else if (itemDto.getAvailable() == null && itemDto.getDescription() == null) {
            item.setName(itemDto.getName());
        } else if (itemDto.getName() == null && itemDto.getDescription() == null) {
            item.setAvailable(itemDto.getAvailable());
        } else {
            item.setDescription(itemDto.getDescription());
            item.setName(itemDto.getName());
            item.setAvailable(itemDto.getAvailable());
        }
        return item;
    }

    @Override
    public Item getItemById(long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Вещи с данным id не существует!");
        }
        return items.get(itemId);
    }

    @Override
    public List<Item> getItemsByOwnerId(long ownerId) {
        if (userService.getUserById(ownerId) == null) {
            throw new NotFoundException("Пользователя с таким id не существует!");
        }
        List<Item> userItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().getId() == ownerId) {
                userItems.add(item);
            }
        }
        return userItems;
    }

    @Override
    public List<Item> search(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        String lowerText = text.toLowerCase();
        return items.values().stream()
                .filter(Item::isAvailable)
                .filter(item -> item.getName().toLowerCase().contains(lowerText) ||
                        item.getDescription().toLowerCase().contains(lowerText))
                .collect(toList());
    }

}
