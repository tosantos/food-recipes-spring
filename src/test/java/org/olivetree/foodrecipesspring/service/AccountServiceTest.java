package org.olivetree.foodrecipesspring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.olivetree.foodrecipesspring.domain.Account;
import org.olivetree.foodrecipesspring.domain.ResetToken;
import org.olivetree.foodrecipesspring.domain.User;
import org.olivetree.foodrecipesspring.domain.VerificationToken;
import org.olivetree.foodrecipesspring.events.OnCreateAccountEvent;
import org.olivetree.foodrecipesspring.events.OnResetPasswordEvent;
import org.olivetree.foodrecipesspring.exception.AccountAlreadyExistsException;
import org.olivetree.foodrecipesspring.exception.PasswordMismatchException;
import org.olivetree.foodrecipesspring.model.AccountDto;
import org.olivetree.foodrecipesspring.model.PasswordDto;
import org.olivetree.foodrecipesspring.repository.AccountRepository;
import org.olivetree.foodrecipesspring.repository.ResetTokenRepository;
import org.olivetree.foodrecipesspring.repository.VerificationTokenRepository;
import org.olivetree.foodrecipesspring.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private ResetTokenRepository resetTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private static final String USERNAME = "tosantos";
    private static final String FIRSTNAME = "Thiago";
    private static final String LASTNAME = "Santos";
    private static final String EMAIL = "thiago@company.com";


    @BeforeEach() void setup() {
        accountService = new AccountServiceImpl(accountRepository, verificationTokenRepository, resetTokenRepository, userRepository, encoder, eventPublisher);
    }

    @Test
    @DisplayName("Given Account details, when account is created then account is persisted")
    public void givenAccountDetailsWhenAccountIsCreatedThenAccountIsPersisted() {

        String password = "aaa";
        String passwordconfirm = "aaa";

        AccountDto accountDto = new AccountDto(USERNAME, FIRSTNAME, LASTNAME, password, passwordconfirm, EMAIL);
        when(accountRepository.findByUsernameOrEmail(accountDto.username(), accountDto.email())).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("aaa");

        Account account = getAccountFromRepository(USERNAME, FIRSTNAME, LASTNAME, EMAIL);

        when(accountRepository.saveAndFlush(any(Account.class)))
                .thenReturn(account);

        accountService.createAccount(accountDto);

        verify(accountRepository, times(1)).saveAndFlush(any(Account.class));
        verify(eventPublisher, times(1)).publishEvent(any(OnCreateAccountEvent.class));
    }

    @Test
    @DisplayName("Given Account without matching password, when account is created then a PasswordMismatchException is thrown")
    public void givenAccountWithoutMatchingPasswordWhenAccountIsCreatedThenThrowException() {
        String password = "aaa";
        String passwordconfirm = "bbb";

        AccountDto accountDto = new AccountDto(USERNAME, FIRSTNAME, LASTNAME, password, passwordconfirm, EMAIL);
        when(accountRepository.findByUsernameOrEmail(accountDto.username(), accountDto.email())).thenReturn(Optional.empty());

        assertThrows(PasswordMismatchException.class, () -> accountService.createAccount(accountDto));
    }

    @Test
    @DisplayName("Given Account with existing username, when account is created then a AccountAlreadyExistsException is thrown")
    public void givenAccountWithExistingUsernameWhenAccountIsCreatedThenThrowException() {
        String password = "aaa";
        String passwordconfirm = "aaa";

        AccountDto accountDto = new AccountDto(USERNAME, FIRSTNAME, LASTNAME, password, passwordconfirm, EMAIL);

        when(accountRepository.findByUsernameOrEmail(accountDto.username(), accountDto.email()))
                .thenReturn(Optional.of(getAccountFromRepository(USERNAME, FIRSTNAME, LASTNAME, EMAIL)));

        assertThrows(AccountAlreadyExistsException.class, () -> accountService.createAccount(accountDto));
    }

    @Test
    @DisplayName("Given Account username and random token when verification token is created then token is persisted")
    public void givenAccountUsernameAndRandomTokenWhenVerificationTokenIsCreatedThenTokenIsPersisted() {
        String username = "username";

        accountService.createVerificationToken(username, "randomToken");

        verify(verificationTokenRepository, times(1)).saveAndFlush(any(VerificationToken.class));
    }

    @Test
    @DisplayName("Given Account username when account is confirmed then account is enabled")
    public void givenAccountUsernameWhenAccountIsConfirmedThenAccountIsEnabled() {
        String username = "username";

        User user = new User();
        user.setUsername(username);
        user.setEnabled(false);

        when(userRepository.findById(username))
                .thenReturn(Optional.of(user));

        accountService.confirmUserAccount(username);

        verify(userRepository, times(1)).saveAndFlush(any(User.class));
        verify(verificationTokenRepository, times(1)).deleteByTokenIdUsername(username);

        // Capture the argument for User when saving
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).saveAndFlush(userCaptor.capture());
        assertTrue(userCaptor.getValue().getEnabled());
    }

    @Test
    @DisplayName("Given Account username when confirming an account, account is not found then a RuntimeException is thrown")
    public void givenAccountUsernameWhenConfirmingAccountIsNotFoundThenRuntimeExceptionIsThrown() {
        String username = "username";

        when(userRepository.findById(username))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> accountService.confirmUserAccount(username));
    }

    @Test
    @DisplayName("Given Account username when account is deleted then account is deleted")
    public void givenAccountUsernameWhenAccountIsDeletedThenAccountIsDeleted() {
        Account account = getAccountFromRepository(USERNAME, FIRSTNAME, LASTNAME, EMAIL);

        when(accountRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(account));

        accountService.deleteAccount(USERNAME);

        verify(userRepository, times(1)).deleteById(USERNAME);
        verify(accountRepository, times(1)).delete(account);
        verify(verificationTokenRepository, times(1)).deleteByTokenIdUsername(USERNAME);
    }

    @Test
    @DisplayName("Given Account username when account is not found then a RuntimeException is thrown")
    public void givenAccountUsernameWhenAccountIsNotFoundThenThrowException() {
        when(accountRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> accountService.deleteAccount(USERNAME));
    }

    @Test
    @DisplayName("Given Account email that exists when reset account then a OnResetPasswordEvent is raised")
    public void givenAccountEmailThatExistsWhenResetAccountThenOnResetPasswordEventRaised() {

        Account account = getAccountFromRepository(USERNAME, FIRSTNAME, LASTNAME, EMAIL);

        when(accountRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(account));

        accountService.resetAccount(EMAIL);

        verify(eventPublisher, times(1)).publishEvent(any(OnResetPasswordEvent.class));
    }

    @Test
    @DisplayName("Given Account email that does not exist when reset account then a no event is raised")
    public void givenAccountEmailThatDoesNotExistWhenResetAccountThenNoEventRaised() {
        accountService.resetAccount(EMAIL);
        verify(eventPublisher, times(0)).publishEvent(any(OnResetPasswordEvent.class));
    }

    @Test
    @DisplayName("Given Account username and random token when reset token is created then token is persisted")
    public void givenAccountUsernameAndRandomTokenWhenResetTokenIsCreatedThenTokenIsPersisted() {
        accountService.createPasswordResetToken(USERNAME, "randomToken");

        verify(resetTokenRepository, times(1)).saveAndFlush(any(ResetToken.class));
    }

    @Test
    @DisplayName("Given Account username and password when account password is reset then account password is reset")
    public void givenAccountUsernameAndPasswordWhenAccountPasswordIsResetThenAccountPasswordIsReset() {
        String password = "aaa";
        String passwordconfirm = "aaa";

        User user = new User();
        user.setUsername(USERNAME);
        user.setEnabled(false);

        when(userRepository.findById(USERNAME))
                .thenReturn(Optional.of(user));

        when(encoder.encode(password)).thenReturn("aaa-encoded");

        PasswordDto passwordDto = new PasswordDto(USERNAME, password, passwordconfirm);

        accountService.resetAccountPassword(passwordDto);

        verify(userRepository, times(1)).saveAndFlush(any(User.class));
        verify(resetTokenRepository, times(1)).deleteByTokenIdUsername(USERNAME);

        // Capture the argument for Account when saving
        ArgumentCaptor<User> accountCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).saveAndFlush(accountCaptor.capture());
        assertTrue(accountCaptor.getValue().getPassword().equals("aaa-encoded"));
    }

    @Test
    @DisplayName("Given Account username and password when account password is reset and passwords do not match then a PasswordMismatchException is thrown")
    public void givenAccountUsernameAndPasswordWhenAccountPasswordIsResetAndPasswordsDoNotMatchThenThrowException() {
        String password = "aaa";
        String passwordconfirm = "bbb";

        PasswordDto passwordDto = new PasswordDto(USERNAME, password, passwordconfirm);

        assertThrows(PasswordMismatchException.class, () -> accountService.resetAccountPassword(passwordDto));
    }

    private Account getAccountFromRepository(String username, String firstName, String lastName, String email) {
        Account account = new Account();
        account.setUsername(username);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setEmail(email);
        return account;
    }
}
