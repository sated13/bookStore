package ru.alex.bookStore.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_Id", unique = true, nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    @Getter @Setter private String username;

    @Column(nullable = false, length = 160)
    @Getter @Setter private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_Id", referencedColumnName = "user_Id"),
            inverseJoinColumns = @JoinColumn(name = "user_role_id", referencedColumnName = "user_role_id"))
    @Getter @Setter private Set<UserRole> roles;

    @Column(nullable = false)
    @Getter @Setter private boolean enabled;

    public void addRole(UserRole Role) {
        this.roles.add(Role);
    }

    public void deleteRole(UserRole role) {
        this.roles.remove(role);
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;

        User user = (User) obj;
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("Username: ").append(this.username).append("; ");
        sb.append("Password: [PROTECTED]; ");
        sb.append("Enabled: ").append(this.enabled).append("; ");

        if (!roles.isEmpty()) {
            sb.append("Granted Authorities: ");

            boolean first = true;
            for (GrantedAuthority auth : roles) {
                if (!first) {
                    sb.append(",");
                }
                first = false;

                sb.append(auth);
            }
        } else {
            sb.append("Not granted any authorities");
        }

        return sb.toString();
    }
}
