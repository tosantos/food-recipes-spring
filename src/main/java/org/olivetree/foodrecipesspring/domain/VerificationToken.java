package org.olivetree.foodrecipesspring.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "verification_tokens")
public class VerificationToken {
    @EmbeddedId
    private VerificationTokenPK tokenId;
    private LocalDateTime expiryDate;

    public VerificationToken() {
    }

    public VerificationTokenPK getTokenId() {
        return tokenId;
    }

    public void setTokenId(VerificationTokenPK tokenId) {
        this.tokenId = tokenId;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
