package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private Map<String, User> usersByEmail = new HashMap<>();

    private Map<Long, User> usersById = new HashMap<>();

    @Override
    public User createUser(UserDto userDto) {
        validate(userDto);
        if (usersByEmail.containsKey(userDto.getEmail())) {
            throw new AlreadyExistsException("Пользователь с таким email уже существует!");
        }
        User newUser = UserMapper.fromDto(userDto);
        usersByEmail.put(newUser.getEmail(), newUser);
        usersById.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User updateUser(UserDto userDto, long userId) {
        if (usersByEmail.containsKey(userDto.getEmail())) {
            throw new AlreadyExistsException("Пользователь с таким email уже существует!");
        }
        User user = getUserById(userId);

        if (userDto.getName() == null) {
            user.setEmail(userDto.getEmail());
        } else if (userDto.getEmail() == null) {
            user.setName(userDto.getName());
        } else {
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
        }

        return user;
    }


    @Override
    public User getUserById(long userId) {
        if (!usersById.containsKey(userId)) {
            throw new NotFoundException("Пользователя с указанным id не существует!");
        }
        return usersById.get(userId);
    }

    @Override
    public void deleteUserById(long userId) {
        if (!usersById.containsKey(userId)) {
            throw new NotFoundException("Пользователя с указанным id не существует!");
        }
        User user = usersById.get(userId);
        usersByEmail.remove(user.getEmail());
        usersById.remove(userId);
    }

    private void validate(UserDto userDto) throws ValidateException {
        if (userDto.getEmail().isBlank() || userDto.getEmail() == null) {
            throw new ValidateException("Неверный email пользователя!");
        }
    }
}
