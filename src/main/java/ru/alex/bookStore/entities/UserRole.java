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

    public UserRole() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }

    @Override
    public int hashCode() {
        return role.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return role.equals(((UserRole)obj).getRole());
    }
}
