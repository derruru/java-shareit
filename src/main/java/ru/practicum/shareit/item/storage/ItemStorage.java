package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item createItem(long userId, Item item);

    Item updateItem(long userId, long itemId, Item item);

    Item getItemById(long userId, long itemId);

    List<Item> getAllUserItems(long userId);

    List<Item> search(String text);
}
