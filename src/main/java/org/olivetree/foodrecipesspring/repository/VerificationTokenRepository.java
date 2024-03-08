package org.olivetree.foodrecipesspring.repository;

import org.olivetree.foodrecipesspring.domain.VerificationToken;
import org.olivetree.foodrecipesspring.domain.VerificationTokenPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, VerificationTokenPK> {
    VerificationToken findByTokenIdToken(String token);

    void deleteByTokenIdUsername(String username);
}
