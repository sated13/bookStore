package ru.alex.bookStore.entities;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 160)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<UserRole> roles;

    public User() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserRole> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public void addUserRoles(UserRole userRole) {
        this.roles.add(userRole);
    }
}
