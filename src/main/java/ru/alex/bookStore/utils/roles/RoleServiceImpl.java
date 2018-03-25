package ru.alex.bookStore.utils.roles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
        try {
            if (null == findByRole(role)) {
                UserRole userRole = new UserRole();
                userRole.setAuthority(role);
                roleRepository.save(userRole);
                return true;
            } else return false;
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

    @Override
    public long countRoles() {
        try {
            return roleRepository.count();
        } catch (Exception e) {
            //ToDo: add logging
            return 0;
        }
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
    public boolean changeRoleDetails(UserRole role, String roleName/*, Set<User> users*/) {
        try {
            if (role != null) {
                role.setAuthority(roleName);
                roleRepository.save(role);
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
