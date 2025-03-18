package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

@Data
public class ItemDto {

    private String name;

    private String description;

    private Boolean available;

    private ItemRequest itemRequest;

    public ItemDto() {
    }

    public ItemDto(String name, String description, boolean available, ItemRequest itemRequest) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.itemRequest = itemRequest;
    }

    public boolean isAvailable() {
        return available;
    }

}
