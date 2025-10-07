package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemStorage itemStorage;

    private final UserService userService;

    private final BookingStorage bookingStorage;

    private final CommentStorage commentStorage;

    @Transactional
    public ItemDto createItem(long userId, ItemDto itemDto) {
        userService.getUserById(userId);
        Item item = ItemMapper.fromItemDto(itemDto);
        item.setUserId(userId);
        return ItemMapper.toItemDto(itemStorage.save(item));
    }

    @Transactional
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        userService.isExists(userId);
        this.isExists(itemId);
        Item tableItem = ItemMapper.fromItemDtoWithId(this.getItemById(userId, itemId));
        Item item = ItemMapper.fromItemDto(itemDto);
        if (item.getName() != null) {
            tableItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            tableItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            tableItem.setAvailable(item.getAvailable());
        }
        return ItemMapper.toItemDto(itemStorage.save(tableItem));
    }

    @Transactional
    public ItemDto getItemById(long userId, long itemId) {
        userService.getUserById(userId);
        Item item = itemStorage.findById(itemId).get();
        List<Comment> comments = commentStorage.findByItemId(itemId);
        if (!comments.isEmpty()) {
            item.setComments(comments);
        }
        return ItemMapper.toItemDto(item);
    }

    @Transactional
    public List<ItemDto> getAllUserItems(long userId) {
        userService.getUserById(userId);
        return itemStorage.findAllByUserId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemStorage.search(text).stream()
                .map(this::addBookingDates)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto createComment(long userId, long itemId, CommentDto commentDto) {
        List<Booking> bookings = bookingStorage.getByItemIdAndBookerIdAndEndBefore(itemId, userId, LocalDateTime.now());
        if (bookings.isEmpty()) {
            throw new ValidationException("Пользователь не брал вещь в аренду!");
        }
        Comment comment = CommentMapper.fromCommentDto(commentDto);
        comment.setItem(ItemMapper.fromItemDtoWithId(this.getItemById(userId, itemId)));
        comment.setAuthor(UserMapper.fromUserDtoWithId(userService.getUserById(userId)));
        Comment addedComment = commentStorage.save(comment);
        return CommentMapper.toCommentDto(addedComment);
    }

    public boolean isAvailable(long userId, long itemId) {
        return this.getItemById(userId, itemId).getAvailable();
    }

    public boolean isExists(long itemId) {
        return itemStorage.existsById(itemId);
    }

    private Item addBookingDates(Item item) {
        List<Booking> bookings = bookingStorage.getByItemId(item.getId());
        if (bookings.isEmpty()) {
            return item;
        }
        Booking lastBooking = bookings.stream()
                .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now())).max(new Comparator<Booking>() {
                    @Override
                    public int compare(Booking o1, Booking o2) {
                        return o1.getEnd().compareTo(o2.getEnd());
                    }
                }).get();
        item.setLastBooking(lastBooking.getStart());

        Booking nextBooking = bookings.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now())).min(new Comparator<Booking>() {
                    @Override
                    public int compare(Booking o1, Booking o2) {
                        return o1.getEnd().compareTo(o2.getEnd());
                    }
                }).get();
        item.setNextBooking(nextBooking.getStart());

        return item;
    }
}