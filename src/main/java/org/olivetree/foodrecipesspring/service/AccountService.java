package org.olivetree.foodrecipesspring.service;

import org.olivetree.foodrecipesspring.domain.Account;
import org.olivetree.foodrecipesspring.model.AccountDto;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    void createVerificationToken(String token, Account account);
}
