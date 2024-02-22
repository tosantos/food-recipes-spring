package org.olivetree.foodrecipesspring.service;

import org.olivetree.foodrecipesspring.domain.Account;

public interface VerificationTokenService {
    void createVerificationToken(String token, Account account);
}
