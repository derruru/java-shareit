package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
public class Item {

    private long id;

    private String name;

    private String description;

    private boolean available;

    private User owner;

    private ItemRequest request;

    public Item() {}

    public Item(long id, String name, String description, boolean available, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }

}
