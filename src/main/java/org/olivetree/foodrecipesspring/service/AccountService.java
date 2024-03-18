package org.olivetree.foodrecipesspring.service;

import org.olivetree.foodrecipesspring.domain.ResetToken;
import org.olivetree.foodrecipesspring.domain.VerificationToken;
import org.olivetree.foodrecipesspring.model.AccountDto;
import org.olivetree.foodrecipesspring.model.PasswordDto;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    void createVerificationToken(String username, String token);

    void confirmUserAccount(String username);

    VerificationToken findByToken(String token);

    ResetToken findByResetToken(String token);

    void deleteAccount(String username);

    void resetAccount(String email);

    void createPasswordResetToken(String username, String token);

    void resetAccountPassword(PasswordDto password);
}
