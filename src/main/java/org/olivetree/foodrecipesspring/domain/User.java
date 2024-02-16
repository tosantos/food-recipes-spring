package org.olivetree.foodrecipesspring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;

@Entity(name = "users")
public class User {
    @Id
    UUID id;

    @Column
    String username;

    @Column
    String password;

    @Column
    Boolean enabled = true;

    @OneToMany(cascade=ALL, fetch=EAGER)
    List<UserAuthority> userAuthorities = new ArrayList<>();

    public User() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<UserAuthority> getUserAuthorities() {
        return userAuthorities;
    }

    public void setUserAuthorities(List<UserAuthority> userAuthorities) {
        this.userAuthorities = userAuthorities;
    }
}
