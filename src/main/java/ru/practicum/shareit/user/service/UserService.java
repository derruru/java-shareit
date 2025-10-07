package ru.practicum.shareit.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    @Transactional
    public UserDto createUser(UserDto userDto) {
        return UserMapper.toUserDto(userStorage.save(UserMapper.fromUserDto(userDto)));
    }

    @Transactional
    public UserDto updateUser(long userId, UserDto userDto) {
        User tableUser = UserMapper.fromUserDtoWithId(this.getUserById(userId));
        User user = UserMapper.fromUserDto(userDto);
        if (user.getName() != null) {
            tableUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            tableUser.setEmail(user.getEmail());
        }
        return UserMapper.toUserDto(userStorage.save(tableUser));
    }


    @Transactional
    public UserDto getUserById(long userId) {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException("Такого пользователя не существует!");
        }
        return UserMapper.toUserDto(userStorage.findById(userId).get());
    }

    @Transactional
    public void deleteUser(long userId) {
        userStorage.deleteById(userId);
    }

    public boolean isExists(long userId) {
        return userStorage.existsById(userId);
    }

}