package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    private static long id = 0;

    public static ItemDto toDto(Item item) {
        return new ItemDto(
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequest() != null ? item.getRequest() : null
        );
    }

    public static Item fromDto(ItemDto itemDto) {
        return new Item(++id,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.isAvailable(),
                itemDto.getItemRequest() != null ? itemDto.getItemRequest() : null);
    }

}
