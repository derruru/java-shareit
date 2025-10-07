package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;

@Component
public class ItemMapper {

    public static Item fromItemDto(ItemDto itemDto) {
        return new Item(itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable());
    }

    public static Item fromItemDtoWithId(ItemDto itemDto) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(),
                itemDto.getUserId());
    }

    public static ItemDto toItemDto(Item item) {
        if (item.getComments() == null || item.getComments().isEmpty()) {
            return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getUserId(),
                    item.getLastBooking(), item.getNextBooking(), new ArrayList<>());
        }
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getUserId(),
                item.getLastBooking(), item.getNextBooking(), item.getComments().stream().map(CommentMapper::toCommentDto).toList());
    }
}
