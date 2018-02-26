package ru.alex.bookStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alex.bookStore.entities.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByRole(String role);
}
