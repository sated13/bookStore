package ru.alex.bookStore.utils.users;

import ru.alex.bookStore.entities.User;
import ru.alex.bookStore.entities.UserRole;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserService {

    boolean save(String username, String password, Set<UserRole> roles);

    boolean delete(String username);

    boolean delete(User user);

    int delete(Set<String> users);

    long countUsers();

    User findByUsername(String username);

    Set<User> findByUserNames(Set<String> userNames);

    Map<UserRole, Set<User>> getUsersByRoles();

    Map<UserRole, Integer> getCountOfUsersByRoles();

    Integer countOfUsersWithRole(UserRole role);

    List<User> getAllUsers();

    List<String> getAllUserNames();

    boolean passwordIsCorrect(String password, String hashedPassword);

    String encryptPassword(String plainPassword);

    boolean isAdmin(String username);

    boolean isAdmin(User user);

    boolean changeUserDetails(String user, String newUserName, String password, Set<UserRole> roles);

    Set<User> findUsersByRolesContains(UserRole role);

    int addRoleToUsers(UserRole role, Set<User> users);

    /**
     * Set role only on users from Set
     * @param role  role for users
     * @param users only users from this set should have this role
     */
    int setRoleOnUsers(UserRole role, Set<User> users);
}
