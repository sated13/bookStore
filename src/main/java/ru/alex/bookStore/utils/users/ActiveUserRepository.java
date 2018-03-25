package ru.alex.bookStore.utils.users;

import ru.alex.bookStore.entities.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ActiveUserRepository {

    private Set<User> activeUsers;

    public ActiveUserRepository() {
        activeUsers = new HashSet<>();
    }

    public Set<User> getActiveUsers() {
        return Collections.unmodifiableSet(activeUsers);
    }

    public void addActiveUsers(User activeUser) {
        activeUsers.add(activeUser);
    }

    public void removeActiveUser(User activeUser) {
        activeUsers.remove(activeUser);
    }
}
