package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class User {

    private long id;

    private String name;

    @Email
    private String email;

    public User() {
    }

    public User(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

}
