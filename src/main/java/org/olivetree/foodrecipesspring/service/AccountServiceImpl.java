package org.olivetree.foodrecipesspring.service;

import org.olivetree.foodrecipesspring.domain.Account;
import org.olivetree.foodrecipesspring.events.OnCreateAccountEvent;
import org.olivetree.foodrecipesspring.exception.AccountAlreadyExistsException;
import org.olivetree.foodrecipesspring.exception.PasswordMismatchException;
import org.olivetree.foodrecipesspring.model.AccountDto;
import org.olivetree.foodrecipesspring.repository.AccountRepository;
import org.olivetree.foodrecipesspring.util.ErrorMessages;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final PasswordEncoder encoder;

    private final AccountRepository accountRepository;

    private final ApplicationEventPublisher eventPublisher;

    public AccountServiceImpl(AccountRepository accountRepository,
                              PasswordEncoder encoder,
                              ApplicationEventPublisher eventPublisher) {
        this.accountRepository = accountRepository;
        this.encoder = encoder;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {

        //should verify that the account doesn't already exist
        accountRepository.findByUsernameOrEmail(accountDto.username(), accountDto.email())
                .ifPresent(acc -> {
                    throw new AccountAlreadyExistsException(ErrorMessages.ACCOUNT_ALREADY_EXISTS);
                });

        //should verify that the password and the password confirmation match
        if(!accountDto.password().equals(accountDto.passwordconfirm())) {
            throw new PasswordMismatchException(ErrorMessages.PASSWORDS_MISMATCH);
        }

        Account account = getAccount(accountDto);

        // Encrypt the password
        account.setPassword(encoder.encode(account.getPassword()));

        Account createdAccount = accountRepository.saveAndFlush(account);

        // Fire on create account event
        eventPublisher.publishEvent(new OnCreateAccountEvent(account));

        return convertToDto(createdAccount);
    }

    private AccountDto convertToDto(Account createdAccount) {
        return new AccountDto(
                createdAccount.getUsername(),
                createdAccount.getFirstName(),
                createdAccount.getLastName(),
                null,
                null,
                createdAccount.getEmail()
        );
    }

    private Account getAccount(AccountDto accountDto) {
        Account account = new Account();
        account.setEmail(accountDto.email());
        account.setPassword(accountDto.password());
        account.setFirstName(accountDto.firstname());
        account.setLastName(accountDto.lastname());
        account.setUsername(accountDto.username());
        return account;
    }
}
