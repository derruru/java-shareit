package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.dto.OutputBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public OutputBookingDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @RequestBody InputBookingDto bookingDto) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public OutputBookingDto approved(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId,
                                     @RequestParam boolean approved) {
        return bookingService.approved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public OutputBookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<OutputBookingDto> getByUserIdAndState(@RequestParam(defaultValue = "ALL") String state,
                                                      @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getByUserIdAndState(userId, state);
    }

    @GetMapping("/owner")
    public List<OutputBookingDto> getByOwnerId(@RequestParam(defaultValue = "ALL") String state,
                                               @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getByOwnerId(state, userId);
    }

}
