package ru.alex.bookStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.alex.bookStore.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    //@Query("select u from users u where u.username = ?1")
    User findByUsername(String username);

}
