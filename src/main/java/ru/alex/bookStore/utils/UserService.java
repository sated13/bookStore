package ru.alex.bookStore.utils;

import ru.alex.bookStore.entities.User;

public interface UserService {

    void save(User user);
    User findByUsername(String username);
}
