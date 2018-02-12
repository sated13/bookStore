package ru.alex.bookStore.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String userName;

    @Column(nullable = false, length = 60)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<UserRole> userRoles;

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

    public List<UserRole> getUserRoles() {
        return Collections.unmodifiableList(userRoles);
    }

    public void addUserRoles(UserRole userRole) {
        this.userRoles.add(userRole);
    }
}
