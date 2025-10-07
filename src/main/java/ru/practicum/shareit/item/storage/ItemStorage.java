package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage extends JpaRepository<Item, Long> {

    @Query("SELECT it, b.start, b.end FROM Item as it " +
            "LEFT JOIN Booking as b ON b.item = it " +
            "WHERE it.userId = ?1")
    List<Item> findAllByUserId(long userId);

    @Query("SELECT it FROM Item as it " +
            "WHERE it.available = true AND " +
            "LOWER(it.name) LIKE LOWER(concat('%', ?1, '%')) OR " +
            "LOWER(it.description) LIKE LOWER(concat('%', ?1, '%'))")
    List<Item> search(String text); // потом доделать
}
