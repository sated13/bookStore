package ru.alex.bookStore.utils.roles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.alex.bookStore.entities.User;
import ru.alex.bookStore.entities.UserRole;
import ru.alex.bookStore.repository.RoleRepository;

import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    RoleServiceImpl() {
    }

    public boolean save(String role) {
        UserRole userRole = new UserRole();
        userRole.setAuthority(role);
        try {
            roleRepository.save(userRole);
            return true;
        } catch (Exception e) {
            //ToDo: add logging
            return false;
        }
    }

    public boolean delete(String role) {
        UserRole userRole = findByRole(role);
        try {
            roleRepository.delete(userRole);
            return true;
        } catch (Exception e) {
            //ToDo: add logging
            return false;
        }
    }

    public boolean delete(UserRole role) {
        try {
            roleRepository.delete(role);
            return true;
        } catch (Exception e) {
            //ToDo: add logging
            return false;
        }
    }

    public int delete(Set<String> roles) {
        int result = 0;

        for (String role : roles) {
            result += (delete(role)) ? 1 : 0;
        }

        return result;
    }

    public UserRole findByRole(String role) {
        UserRole userRole = null;
        try {
            userRole = roleRepository.findByAuthority(role);
        } catch (Exception e) {
            //ToDo: add logging
        }
        return userRole;
    }

    @Override
    public Set<UserRole> findByRoles(Set<String> roles) {
        Set<UserRole> foundedRoles = new HashSet<>();
        UserRole tempRole;
        for (String role : roles) {
            tempRole = findByRole(role);
            if (null != tempRole) foundedRoles.add(tempRole);
        }
        return foundedRoles;
    }

    public List<UserRole> getAllRoles() {
        return Collections.unmodifiableList(roleRepository.findAll(new Sort(Sort.Direction.ASC, "authority")));
    }

    @Override
    public List<String> getAllStringRoles() {
        List<UserRole> userRoles = getAllRoles();
        List<String> allStringUserRoles = new ArrayList<>();
        for (UserRole role : userRoles) {
            allStringUserRoles.add(role.getAuthority());
        }

        return allStringUserRoles;
    }

    @Override
    public Set<User> getUsersByRole(String role) {
        return Collections.unmodifiableSet(roleRepository.findByAuthority(role).getUsers());
    }

    @Override
    public boolean changeRoleDetails(String role, String roleName, Set<User> users) {
        try {
            UserRole roleObject = findByRole(role);

            if (roleObject != null) {
                roleObject.setAuthority(roleName);
                roleObject.setUsers(users);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            //ToDo: add logging
            return false;
        }
    }
}
