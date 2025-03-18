package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
public class ItemService {

    private ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item createItem(ItemDto itemDto, long userId) {
        return itemRepository.createItem(itemDto, userId);
    }

    public Item updateItem(ItemDto itemDto, long userId, long itemId) {
        return itemRepository.updateItem(itemDto, userId, itemId);
    }

    public Item getItemById(long itemId) {
        return itemRepository.getItemById(itemId);
    }

    public List<Item> getItemsByOwnerId(long userId) {
        return itemRepository.getItemsByOwnerId(userId);
    }

    public List<Item> search(String text) {
        return itemRepository.search(text);
    }
}
