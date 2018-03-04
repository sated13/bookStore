package ru.alex.bookStore.entities;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {// extends org.springframework.security.core.userdetails.User{

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

    @Column(nullable = false)
    private boolean enabled;

    public User() {
        //this("", "", new HashSet<UserRole>());
    }

    /*public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    @Override*/
    public String getUsername() {
        //return super.getUsername();
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //@Override
    public String getPassword() {
        //return super.getPassword();
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserRole> getRoles() {
        //return super.getAuthorities();
        return roles;
    }

    public void setRoles(Set<UserRole> authorities) {
        this.roles = authorities;
    }

    public void addUserRoles(UserRole userRole) {
        this.roles.add(userRole);
    }

    //@Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof org.springframework.security.core.userdetails.User) {
            return username.equals(((User) rhs).username);
        }
        return false;
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
        }
        else {
            sb.append("Not granted any authorities");
        }

        return sb.toString();
    }
}
