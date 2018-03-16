package ru.alex.bookStore.utils.users;

import ru.alex.bookStore.entities.User;
import ru.alex.bookStore.entities.UserRole;

import java.util.List;
import java.util.Set;

public interface UserService {

    boolean save(String username, String password, Set<UserRole> roles);

    boolean delete(String username);

    boolean delete(User user);

    int delete(Set<String> users);

    User findByUsername(String username);

    Set<User> findByUserNames(Set<String> userNames);

    List<User> getAllUsers();

    List<String> getAllUserNames();

    boolean passwordIsCorrect(String password, String hashedPassword);

    boolean isAdmin(String username);

    boolean isAdmin(User user);

    boolean changeUserDetails(String user, String newUserName, String password, Set<UserRole> roles);
}
