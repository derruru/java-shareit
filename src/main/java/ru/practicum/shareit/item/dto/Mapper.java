package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class Mapper {

    public Item fromItemDto(ItemDto itemDto) {
        return new Item(itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable());
    }


}
