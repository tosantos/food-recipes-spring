package org.olivetree.foodrecipesspring.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ResetTokenPK implements Serializable {
    private String token;
    private String username;

    public ResetTokenPK() {
    }

    public ResetTokenPK(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResetTokenPK that = (ResetTokenPK) o;
        return Objects.equals(token, that.token) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, username);
    }
}
