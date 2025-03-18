package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {

    private static long id = 0;

    public static UserDto toDto(User user) {
        return new UserDto(user.getName(),
                user.getEmail());
    }

    public static User fromDto(UserDto userDto) {
        return new User(++id,
                userDto.getName(),
                userDto.getEmail());
    }
}
