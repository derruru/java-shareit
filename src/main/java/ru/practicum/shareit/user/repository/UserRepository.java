package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserRepository {

    User createUser(UserDto userDto);

    User updateUser(UserDto userDto, long userId);

    User getUserById(long userId);

    void deleteUserById(long userId);

}
