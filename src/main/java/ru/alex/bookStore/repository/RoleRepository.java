package ru.alex.bookStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alex.bookStore.entities.UserRole;

public interface RoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByAuthority(String authority);
}
