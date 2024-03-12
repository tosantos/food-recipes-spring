package org.olivetree.foodrecipesspring.service;

import jakarta.transaction.Transactional;
import org.olivetree.foodrecipesspring.domain.Account;
import org.olivetree.foodrecipesspring.domain.User;
import org.olivetree.foodrecipesspring.domain.UserAuthority;
import org.olivetree.foodrecipesspring.domain.VerificationToken;
import org.olivetree.foodrecipesspring.domain.VerificationTokenPK;
import org.olivetree.foodrecipesspring.events.OnCreateAccountEvent;
import org.olivetree.foodrecipesspring.exception.AccountAlreadyExistsException;
import org.olivetree.foodrecipesspring.exception.PasswordMismatchException;
import org.olivetree.foodrecipesspring.model.AccountDto;
import org.olivetree.foodrecipesspring.repository.AccountRepository;
import org.olivetree.foodrecipesspring.repository.UserRepository;
import org.olivetree.foodrecipesspring.repository.VerificationTokenRepository;
import org.olivetree.foodrecipesspring.util.ErrorMessages;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private static final long TOKEN_EXPIRY_IN_MINUTES = 30L;

    private final PasswordEncoder encoder;

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final ApplicationEventPublisher eventPublisher;

    public AccountServiceImpl(AccountRepository accountRepository,
                              VerificationTokenRepository verificationTokenRepository,
                              UserRepository userRepository,
                              PasswordEncoder encoder,
                              ApplicationEventPublisher eventPublisher) {
        this.accountRepository = accountRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
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
        Account createdAccount = accountRepository.saveAndFlush(account);

        createUser(accountDto);

        // Fire on create account event
        eventPublisher.publishEvent(new OnCreateAccountEvent(account));

        return convertToDto(createdAccount);
    }

    @Override
    public void createVerificationToken(String username, String token) {
        VerificationTokenPK pk = new VerificationTokenPK();
        pk.setToken(token);
        pk.setUsername(username);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setTokenId(pk);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(TOKEN_EXPIRY_IN_MINUTES));

        verificationTokenRepository.saveAndFlush(verificationToken);
    }

    @Override
    @Transactional
    public void confirmAccount(String username) {
        Optional<User> userOptional = userRepository.findById(username);

        if(userOptional.isEmpty()) {
            throw new RuntimeException("Account not found");
        }

        // Enable the user
        User user = userOptional.get();
        user.setEnabled(true);
        userRepository.saveAndFlush(user);

        // Delete verification token for username
        verificationTokenRepository.deleteByTokenIdUsername(username);
    }

    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findByTokenIdToken(token);
    }

    @Override
    @Transactional
    public void deleteAccount(String username) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);

        if(accountOptional.isEmpty()) {
            throw new RuntimeException("Account not found");
        }

        Account account = accountOptional.get();

        // Delete from users
        userRepository.deleteById(username);

        // Delete from accounts
        accountRepository.delete(account);

        // Delete verification token for username
        verificationTokenRepository.deleteByTokenIdUsername(username);
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
        account.setFirstName(accountDto.firstname());
        account.setLastName(accountDto.lastname());
        account.setUsername(accountDto.username());
        return account;
    }

    private void createUser(AccountDto accountDto) {
        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setAuthority("ROLE_USER");

        User user = new User();
        user.setUsername(accountDto.username());
        user.setPassword(encoder.encode(accountDto.password()));
        user.getUserAuthorities().add(userAuthority);
        user.setEnabled(false);

        userAuthority.setUser(user);

        // Save user and user authority
        userRepository.saveAndFlush(user);
    }
}
