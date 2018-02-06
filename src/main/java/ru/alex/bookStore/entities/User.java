package ru.alex.bookStore.entities;

import org.hibernate.mapping.Collection;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    String userName;

    @Column(nullable = false, length = 60)
    String password;

    @OneToMany(fetch = FetchType.EAGER)
    Set<UserRole> userRoles;

    User() {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserRole> getUserRoles() {
        return Collections.unmodifiableSet(userRoles);
    }

    public void addUserRoles(UserRole userRole) {
        this.userRoles.add(userRole);
    }
}
