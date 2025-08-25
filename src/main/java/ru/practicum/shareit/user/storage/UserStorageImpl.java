package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserStorageImpl implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    private long id = 1;

    @Override
    public User createUser(User user) {
        valid(user);
        user.setId(id++);
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public User updateUser(long userId, User user) {
        if (!this.isExist(userId)) {
            throw new NotFoundException("Пользователя с таким id не существует!");
        }
        valid(user);
        users.remove(userId);
        user.setId(userId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(long userId) {
        if (!this.isExist(userId)) {
            throw new NotFoundException("Пользователя с таким id не существует!");
        }
        return users.get(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUser(long userId) {
        if (!this.isExist(userId)) {
            throw new NotFoundException("Пользователя с таким id не существует!");
        }
        users.remove(userId);
    }

    @Override
    public boolean isExist(long userId) {
        return users.containsKey(userId);
    }

    private void valid(User user) {
        if (emails.contains(user.getEmail())) {
            throw new AlreadyExistException("Пользователь с таким именем уже зарегистрирован!");
        }

    }

}
