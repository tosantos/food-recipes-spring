package org.olivetree.foodrecipesspring.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity(name="authorities")
public class UserAuthority {

    @Id
    @ManyToOne
    @JoinColumn(name="username")
    User user;

    @Column
    String authority;

    public UserAuthority() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
