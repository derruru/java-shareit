package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.List;

@Service
public class ItemService {

    private final ItemStorage itemStorage;

    @Autowired
    public ItemService(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    public Item createItem(long userId, Item item) {
        return itemStorage.createItem(userId, item);
    }

    public Item updateItem(long userId, long itemId, Item item) {
        return itemStorage.updateItem(userId, itemId, item);
    }

    public Item getItemById(long userId, long itemId) {
        return itemStorage.getItemById(userId, itemId);
    }

    public List<Item> getAllUserItems(long userId) {
        return itemStorage.getAllUserItems(userId);
    }

    public List<Item> search(String text) {
        return itemStorage.search(text);
    }
}