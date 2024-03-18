package org.olivetree.foodrecipesspring.repository;

import org.olivetree.foodrecipesspring.domain.ResetToken;
import org.olivetree.foodrecipesspring.domain.ResetTokenPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetTokenRepository extends JpaRepository<ResetToken, ResetTokenPK> {
    ResetToken findByTokenIdToken(String token);

    void deleteByTokenIdUsername(String username);
}
