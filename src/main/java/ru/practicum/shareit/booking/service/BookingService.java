package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.dto.OutputBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingStorage bookingStorage;

    private final UserService userService;

    private final ItemService itemService;

    @Transactional
    public OutputBookingDto create(long userId, InputBookingDto bookingDto) {
        if (!userService.isExists(userId)) {
            throw new NotFoundException("Такого пользователя не существует!");
        }
        if (!itemService.isExists(bookingDto.getItemId())) {
            throw new NotFoundException("Такой вещи не существует!");
        }
        if (!itemService.isAvailable(userId, bookingDto.getItemId())) {
            throw new ValidationException("Вещь недоступна для аренды!");
        }
        Booking booking = new Booking(bookingDto.getStart(), bookingDto.getEnd(),
                ItemMapper.fromItemDtoWithId(itemService.getItemById(userId, bookingDto.getItemId())));
        booking.setBooker(UserMapper.fromUserDtoWithId(userService.getUserById(userId)));
        booking.setStatus(Status.WAITING);
        return BookingMapper.toBookingDto(bookingStorage.save(booking));
    }

    @Transactional
    public OutputBookingDto approved(long userId, long bookingId, boolean approved) {
        if (!userService.isExists(userId)) {
            throw new ValidationException("Такого пользователя не существует!");
        }
        if (!this.isExists(bookingId)) {
            throw new NotFoundException("Такого бронирования не существует!");
        }
        OutputBookingDto outputBookingDto = this.getBookingDto(bookingId);
        if (approved) {
            outputBookingDto.setStatus(Status.APPROVED);
            bookingStorage.approved(Status.APPROVED, bookingId);
        } else {
            outputBookingDto.setStatus(Status.REJECTED);
            bookingStorage.approved(Status.REJECTED, bookingId);
        }
        return outputBookingDto;
    }

    @Transactional
    public OutputBookingDto getBookingById(long userId, long bookingId) {
        if (!userService.isExists(userId)) {
            throw new NotFoundException("Такого пользователя не существует!");
        }
        if (!this.isExists(bookingId)) {
            throw new NotFoundException("Такого бронирования не существует!");
        }
        if (userId != this.getBookingDto(bookingId).getBooker().getId() &&
                (userId != this.getBookingDto(bookingId).getItem().getUserId())) {
            throw new AccessDeniedException("Пользователь не может запрашивать данные!");
        }
        return this.getBookingDto(bookingId);
    }

    @Transactional
    public List<OutputBookingDto> getAllByUserId(long userId) {
        if (!userService.isExists(userId)) {
            throw new NotFoundException("Такого пользователя не существует!");
        }
        return bookingStorage.getByBookerId(userId).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<OutputBookingDto> getByUserIdAndState(long userId, String state) {
        if (!userService.isExists(userId)) {
            throw new NotFoundException("Такого пользователя не существует!");
        }
        return switch (state) {
            case "ALL" -> bookingStorage.getByBookerIdOrderByStartDesc(userId).stream()
                    .map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case "CURRENT" -> bookingStorage.getByBookerIdAndStatusAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                            Status.APPROVED, LocalDateTime.now(), LocalDateTime.now()).stream()
                    .map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case "PAST" ->
                    bookingStorage.getByBookerIdAndStatusAndEndBeforeOrderByStartDesc(userId, Status.APPROVED, LocalDateTime.now()).stream()
                            .map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case "FUTURE" -> bookingStorage.getByBookerIdAndStatusAndStartAfterOrderByStartDesc(userId, Status.APPROVED,
                            LocalDateTime.now()).stream()
                    .map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case "WAITING" -> bookingStorage.getByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING).stream()
                    .map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case "REJECTED" -> bookingStorage.getByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED).stream()
                    .map(BookingMapper::toBookingDto).collect(Collectors.toList());
            default -> null;
        };
    }

    public List<OutputBookingDto> getByOwnerId(String state, long userId) {
        if (!userService.isExists(userId)) {
            throw new NotFoundException("Такого пользователя не существует!");
        }
        if (itemService.getAllUserItems(userId).isEmpty()) {
            throw new ValidationException("У пользователя нет вещей!");
        }
        return switch (state) {
            case "ALL" -> bookingStorage.getByItemUserIdOrderByStartDesc(userId).stream()
                    .map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case "CURRENT" -> bookingStorage.getByItemUserIdAndStatusAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                            Status.APPROVED, LocalDateTime.now(), LocalDateTime.now()).stream()
                    .map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case "PAST" -> bookingStorage.getByItemUserIdAndStatusAndEndBeforeOrderByStartDesc(userId, Status.APPROVED,
                            LocalDateTime.now()).stream()
                    .map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case "FUTURE" ->
                    bookingStorage.getByItemUserIdAndStatusAndStartAfterOrderByStartDesc(userId, Status.APPROVED,
                                    LocalDateTime.now()).stream()
                            .map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case "WAITING" -> bookingStorage.getByItemUserIdAndStatusOrderByStartDesc(userId, Status.WAITING).stream()
                    .map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case "REJECTED" -> bookingStorage.getByItemUserIdAndStatusOrderByStartDesc(userId, Status.REJECTED).stream()
                    .map(BookingMapper::toBookingDto).collect(Collectors.toList());
            default -> null;
        };

    }

    private OutputBookingDto getBookingDto(long bookingId) {
        return BookingMapper.toBookingDto(bookingStorage.findById(bookingId).get());
    }

    private boolean isExists(long bookingId) {
        return bookingStorage.existsById(bookingId);
    }
}
