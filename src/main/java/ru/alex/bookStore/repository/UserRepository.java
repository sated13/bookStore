package ru.alex.bookStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alex.bookStore.entities.User;
import ru.alex.bookStore.entities.UserRole;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    Set<User> findUsersByRolesContains(UserRole role);

    Integer countBooksByRolesContains(UserRole role);
}
