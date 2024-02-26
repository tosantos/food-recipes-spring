package org.olivetree.foodrecipesspring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.olivetree.foodrecipesspring.domain.Account;
import org.olivetree.foodrecipesspring.events.OnCreateAccountEvent;
import org.olivetree.foodrecipesspring.exception.AccountAlreadyExistsException;
import org.olivetree.foodrecipesspring.exception.PasswordMismatchException;
import org.olivetree.foodrecipesspring.model.AccountDto;
import org.olivetree.foodrecipesspring.repository.AccountRepository;
import org.olivetree.foodrecipesspring.repository.VerificationTokenRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @BeforeEach() void setup() {
        accountService = new AccountServiceImpl(accountRepository, verificationTokenRepository, encoder, eventPublisher);
    }

    @Test
    @DisplayName("Given Account details, when account is created then account is persisted")
    public void givenAccountDetailsWhenAccountIsCreatedThenAccountIsPersisted() {

        String username = "username";
        String firstname = "first";
        String lastname = "last";
        String password = "aaa";
        String passwordconfirm = "aaa";
        String email = "some@some.com";

        AccountDto accountDto = new AccountDto(username, firstname, lastname, password, passwordconfirm, email);
        when(accountRepository.findByUsernameOrEmail(accountDto.username(), accountDto.email())).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("aaa");

        Account account = getAccountFromRepository(username, firstname, lastname, password, email);

        when(accountRepository.saveAndFlush(any(Account.class)))
                .thenReturn(account);

        accountService.createAccount(accountDto);

        verify(accountRepository, times(1)).saveAndFlush(any(Account.class));
        verify(eventPublisher, times(1)).publishEvent(any(OnCreateAccountEvent.class));
    }

    @Test
    @DisplayName("Given Account without matching password, when account is created then a PasswordMismatchException is thrown")
    public void givenAccountWithoutMatchingPasswordWhenAccountIsCreatedThenThrowException() {
        String username = "username";
        String firstname = "first";
        String lastname = "last";
        String password = "aaa";
        String passwordconfirm = "bbb";
        String email = "some@some.com";

        AccountDto accountDto = new AccountDto(username, firstname, lastname, password, passwordconfirm, email);
        when(accountRepository.findByUsernameOrEmail(accountDto.username(), accountDto.email())).thenReturn(Optional.empty());

        assertThrows(PasswordMismatchException.class, () -> accountService.createAccount(accountDto));
    }

    @Test
    @DisplayName("Given Account with existing username, when account is created then a AccountAlreadyExistsException is thrown")
    public void givenAccountWithExistingUsernameWhenAccountIsCreatedThenThrowException() {
        String username = "username";
        String firstname = "first";
        String lastname = "last";
        String password = "aaa";
        String passwordconfirm = "aaa";
        String email = "some@some.com";

        AccountDto accountDto = new AccountDto(username, firstname, lastname, password, passwordconfirm, email);

        when(accountRepository.findByUsernameOrEmail(accountDto.username(), accountDto.email()))
                .thenReturn(Optional.of(getAccountFromRepository(username, firstname, lastname, password, email)));

        assertThrows(AccountAlreadyExistsException.class, () -> accountService.createAccount(accountDto));
    }

    private Account getAccountFromRepository(String username, String firstName, String lastName, String password, String email) {
        Account account = new Account();
        account.setUsername(username);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setPassword(password);
        account.setEmail(email);
        return account;
    }
}
