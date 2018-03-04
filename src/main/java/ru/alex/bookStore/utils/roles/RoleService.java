package ru.alex.bookStore.utils.roles;

import ru.alex.bookStore.entities.UserRole;

import java.util.List;
import java.util.Set;

public interface RoleService {

    boolean save(String role);

    boolean delete(String role);

    int delete(Set<String> users);

    UserRole findByRole(String role);

    Set<UserRole> findByRoles(Set<String> roles);

    List<UserRole> getAllRoles();

    List<String> getAllStringRoles();
}
