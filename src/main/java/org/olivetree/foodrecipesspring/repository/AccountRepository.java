package org.olivetree.foodrecipesspring.repository;

import org.olivetree.foodrecipesspring.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsernameOrEmail(String username, String email);
    Optional<Account> findByUsername(String username);
}
