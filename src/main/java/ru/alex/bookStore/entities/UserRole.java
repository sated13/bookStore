package ru.alex.bookStore.entities;

import javax.persistence.*;

@Entity
@Table(name = "user_roles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long userRoleId;

    @Column(nullable = false, length = 45)
    String role;

    public UserRole() {}

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
