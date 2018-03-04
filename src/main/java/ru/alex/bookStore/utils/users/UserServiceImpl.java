package ru.alex.bookStore.utils.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.alex.bookStore.entities.User;
import ru.alex.bookStore.entities.UserRole;
import ru.alex.bookStore.repository.UserRepository;
import ru.alex.bookStore.repository.RoleRepository;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl() {
    }

    public boolean save(String username, String password, Set<UserRole> roles) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);

        try {
            userRepository.save(user);
            return true;
        }
        catch (Exception e) {
            //ToDo: add logging
            return false;
        }
    }

    public boolean delete(String username) {
        User user = findByUsername(username);
        try {
            userRepository.delete(user);
            return true;
        }
        catch (Exception e) {
            //ToDo: add logging
            return false;
        }
    }

    public boolean delete(User user) {
        try {
            userRepository.delete(user);
            return true;
        }
        catch (Exception e) {
            //ToDo: add logging
            return false;
        }
    }

    public int delete(Set<String> users) {
        int result = 0;

        for (String user: users) {
            result += (delete(user)) ? 1 : 0;
        }

        return result;
    }

    public User findByUsername(String username) {
        User user = null;
        try {
            user = userRepository.findByUsername(username);
        }
        catch (Exception e) {
            //ToDo: add logging
        }
        return user;
    }

    public List<User> getAllUsers() {
        return Collections.unmodifiableList(userRepository.findAll(new Sort(Sort.Direction.ASC, "username")));
    }

    public List<String> getAllUsernames() {
        List<User> allUsers = getAllUsers();
        List<String> allUsernames = new ArrayList<>();
        for(User user: allUsers) {
            allUsernames.add(user.getUsername());
        }
        return allUsernames;
    }

    @Override
    public boolean passwordIsCorrect(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }

    @Override
    public boolean isAdmin(String username) {
        GrantedAuthority adminRole = roleRepository.findByAuthority("admin");
        User user = findByUsername(username);
        return user.getRoles().contains(adminRole);
    }

    @Override
    public boolean isAdmin(User user) {
        GrantedAuthority adminRole = roleRepository.findByAuthority("admin");
        return user.getRoles().contains(adminRole);
    }
}
