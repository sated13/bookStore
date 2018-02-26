package ru.alex.bookStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alex.bookStore.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
