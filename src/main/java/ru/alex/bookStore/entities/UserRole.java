package ru.alex.bookStore.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "user_roles")
@NoArgsConstructor
public class UserRole implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id", unique = true, nullable = false)
    private Long userRoleId;

    @Column(nullable = false, length = 45)
    @Getter @Setter private String authority;

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
