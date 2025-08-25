package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class Mapper {

    public User fromUserDto(UserDto userDto) {
        return new User(userDto.getName(), userDto.getEmail());
    }
}
