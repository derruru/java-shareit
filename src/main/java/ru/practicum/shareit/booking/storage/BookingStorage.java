package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingStorage extends JpaRepository<Booking, Long> {

    @Modifying
    @Query("UPDATE Booking b "
            + "SET b.status = :status  "
            + "WHERE b.id = :bookingId")
    void approved(Status status, long bookingId);

    List<Booking> getByBookerId(long userId);

    List<Booking> getByBookerIdOrderByStartDesc(long userId);

    List<Booking> getByBookerIdAndStatusOrderByStartDesc(long userId, Status status);

    List<Booking> getByBookerIdAndStatusAndEndBeforeOrderByStartDesc(long userId, Status status, LocalDateTime date);

    List<Booking> getByBookerIdAndStatusAndStartAfterOrderByStartDesc(long userId, Status status, LocalDateTime date);

    List<Booking> getByBookerIdAndStatusAndStartBeforeAndEndAfterOrderByStartDesc(long userId, Status status,
                                                                                  LocalDateTime forStart, LocalDateTime forEnd);

    List<Booking> getByItemUserIdOrderByStartDesc(long userId);

    List<Booking> getByItemUserIdAndStatusAndStartBeforeAndEndAfterOrderByStartDesc(long userId, Status status,
                                                                                    LocalDateTime forStart, LocalDateTime forEnd);

    List<Booking> getByItemUserIdAndStatusAndEndBeforeOrderByStartDesc(long userId, Status status, LocalDateTime dateTime);

    List<Booking> getByItemUserIdAndStatusAndStartAfterOrderByStartDesc(long userId, Status status, LocalDateTime dateTime);

    List<Booking> getByItemUserIdAndStatusOrderByStartDesc(long userId, Status status);

    List<Booking> getByItemIdAndBookerIdAndEndBefore(long itemId, long userId, LocalDateTime localDateTime);

    List<Booking> getByItemId(long itemId);
}
