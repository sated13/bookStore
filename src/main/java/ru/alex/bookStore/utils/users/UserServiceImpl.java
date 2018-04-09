package ru.alex.bookStore.utils.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.alex.bookStore.entities.User;
import ru.alex.bookStore.entities.UserRole;
import ru.alex.bookStore.repository.UserRepository;
import ru.alex.bookStore.utils.roles.RoleService;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;

    public UserServiceImpl() {
    }

    public boolean save(String username, String password, Set<UserRole> roles) {
        try {
            if (null == findByUsername(username)) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode(password));
                user.setRoles(roles);

                userRepository.save(user);
                return true;
            } else return false;
        } catch (Exception e) {
            //ToDo: add logging
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String username) {
        User user = findByUsername(username);
        try {
            userRepository.delete(user);
            return true;
        } catch (Exception e) {
            //ToDo: add logging
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(User user) {
        try {
            userRepository.delete(user);
            return true;
        } catch (Exception e) {
            //ToDo: add logging
            e.printStackTrace();
            return false;
        }
    }

    public int delete(Set<String> users) {
        int result = 0;

        for (String user : users) {
            result += (delete(user)) ? 1 : 0;
        }

        return result;
    }

    public User findByUsername(String username) {
        User user = null;
        try {
            user = userRepository.findByUsername(username);
        } catch (Exception e) {
            //ToDo: add logging
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public Set<User> findByUserNames(Set<String> userNames) {
        Set<User> users = new HashSet<>();

        try {
            User tempUser;
            for (String username : userNames) {
                tempUser = findByUsername(username);
                if (null != tempUser) {
                    users.add(tempUser);
                }
            }
        } catch (Exception e) {
            //ToDo: add logging
            e.printStackTrace();
            return null;
        }

        return users;
    }

    public List<User> getAllUsers() {
        return Collections.unmodifiableList(userRepository.findAll(new Sort(Sort.Direction.ASC, "username")));
    }

    public List<String> getAllUserNames() {
        List<User> allUsers = getAllUsers();
        List<String> allUsernames = new ArrayList<>();
        for (User user : allUsers) {
            allUsernames.add(user.getUsername());
        }
        return allUsernames;
    }

    @Override
    public boolean passwordIsCorrect(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }

    @Override
    public String encryptPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    @Override
    public boolean isAdmin(String userName) {
        GrantedAuthority adminRole = roleService.findByRole("admin");
        User user = findByUsername(userName);
        return user.getRoles().contains(adminRole);
    }

    @Override
    public boolean isAdmin(User user) {
        GrantedAuthority adminRole = roleService.findByRole("admin");
        return user.getRoles().contains(adminRole);
    }

    @Override
    public boolean changeUserDetails(String user, String newUserName, String password, Set<UserRole> roles) {
        try {
            User userObject = findByUsername(user);
            userObject.setUsername(newUserName);
            userObject.setPassword(passwordEncoder.encode(password));
            userObject.setRoles(roles);
            userRepository.save(userObject);
            return true;
        } catch (Exception e) {
            //ToDo: add logging
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Set<User> findUsersByRolesContains(UserRole role) {
        try {
            return userRepository.findUsersByRolesContains(role);
        } catch (Exception e) {
            //ToDo: add logging
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<UserRole, Set<User>> getUsersByRoles() {
        Map<UserRole, Set<User>> resultMap = new HashMap<>();

        try {
            List<UserRole> roles = roleService.getAllRoles();
            Set<User> setOfUsersForRole;

            for (UserRole role : roles) {
                setOfUsersForRole = findUsersByRolesContains(role);
                resultMap.put(role, setOfUsersForRole);
            }
        }
        catch (Exception e) {
            //ToDo: add logging
            e.printStackTrace();
        }

        return resultMap;
    }

    @Override
    public Map<UserRole, Integer> getCountOfUsersByRoles() {
        Map<UserRole, Integer> resultMap = new HashMap<>();

        try {
            List<UserRole> roles = roleService.getAllRoles();

            for (UserRole role : roles) {
                resultMap.put(role, userRepository.countBooksByRolesContains(role));
            }
        }
        catch (Exception e) {
            //ToDo: add logging
            e.printStackTrace();
        }

        return resultMap;
    }

    @Override
    public Integer countOfUsersWithRole(UserRole role) {
        try {
            return userRepository.countBooksByRolesContains(role);
        }
        catch (Exception e) {
            //ToDo: add logging
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public long countUsers() {
        try {
            return userRepository.count();
        } catch (Exception e) {
            //ToDo: add logging
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int addRoleToUsers(UserRole role, Set<User> users) {
        int countOfChangedUsers = 0;

        for (User user : users) {
            try {
                user.addRole(role);
                userRepository.save(user);
                countOfChangedUsers++;
            } catch (Exception e) {
                //ToDo: add logging
                e.printStackTrace();
            }
        }

        return countOfChangedUsers;
    }

    @Override
    public int setRoleOnUsers(UserRole role, Set<User> users) {
        int countOfChangedUsers = 0;
        Set<User> usersWithRole = findUsersByRolesContains(role);

        for (User user : usersWithRole) {
            try {
                if (!users.contains(user)) {
                    user.deleteRole(role);
                    userRepository.save(user);
                    countOfChangedUsers++;
                }
            } catch (Exception e) {
                //ToDo: add logging
                e.printStackTrace();
            }
        }

        return countOfChangedUsers;
    }
}
