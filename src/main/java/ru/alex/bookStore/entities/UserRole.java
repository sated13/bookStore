package ru.alex.bookStore.entities;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user_roles")
public class UserRole implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id", unique = true, nullable = false)
    private Long userRoleId;

    @Column(nullable = false, length = 45)
    String authority;

    public UserRole() {
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String role) {
        this.authority = role;
    }

    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (this == obj) return true;

        if (!(obj instanceof UserRole)) return false;
        UserRole role = (UserRole) obj;
        return userRoleId.equals(role.userRoleId);
    }

    public int hashCode() {
        return this.authority.hashCode();
    }

    public String toString() {
        return this.authority;
    }
}
