package org.olivetree.foodrecipesspring.service;

import org.olivetree.foodrecipesspring.domain.Account;
import org.olivetree.foodrecipesspring.domain.VerificationToken;
import org.olivetree.foodrecipesspring.model.AccountDto;
import org.olivetree.foodrecipesspring.model.UserAccountDto;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    void createVerificationToken(String username, String token);

    void confirmAccount(String username);

    VerificationToken findByToken(String token);

    void deleteAccount(String username);

    void resetAccount(String email);
}
