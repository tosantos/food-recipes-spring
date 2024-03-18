package org.olivetree.foodrecipesspring.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity(name = "reset_tokens")
public class ResetToken {
    @EmbeddedId
    private ResetTokenPK tokenId;

    private LocalDateTime expiryDate;

    public ResetToken() {
    }

    public ResetTokenPK getTokenId() {
        return tokenId;
    }

    public void setTokenId(ResetTokenPK tokenId) {
        this.tokenId = tokenId;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
