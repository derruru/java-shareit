package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
public class UserService {

    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(UserDto userDto) {
        return repository.createUser(userDto);
    }

    public User updateUser(UserDto userDto, long userId) {
        return repository.updateUser(userDto, userId);
    }

    public User getUserById(long userId) {
        return repository.getUserById(userId);
    }

    public void deleteUserById(long userId) {
        repository.deleteUserById(userId);
    }
}
