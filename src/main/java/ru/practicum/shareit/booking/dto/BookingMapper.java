package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

@Component
public class BookingMapper {

    public static OutputBookingDto toBookingDto(Booking booking) {
        return new OutputBookingDto(booking.getId(), booking.getStart(), booking.getEnd(), booking.getStatus(),
                ItemMapper.toItemDto(booking.getItem()), UserMapper.toUserDto(booking.getBooker()));
    }

}
