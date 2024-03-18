package org.olivetree.foodrecipesspring.controller;

import jakarta.validation.Valid;
import org.olivetree.foodrecipesspring.domain.VerificationToken;
import org.olivetree.foodrecipesspring.exception.AccountAlreadyExistsException;
import org.olivetree.foodrecipesspring.exception.PasswordMismatchException;
import org.olivetree.foodrecipesspring.model.AccountDto;
import org.olivetree.foodrecipesspring.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("account")
    public String getAccount(@ModelAttribute("account")
                                      AccountDto account) {
        return "account";
    }

    @PostMapping("account")
    public String createAccount(@Valid @ModelAttribute("account")
                                      AccountDto account,
                                  BindingResult result) {
        // Check for errors
        if(result.hasErrors()) {
            return "account";
        }

        // Create the account
        try {
            accountService.createAccount(account);
            return "redirect:account";
        } catch (AccountAlreadyExistsException e) {
            result.rejectValue("username", "account.username", e.getMessage());
        } catch (PasswordMismatchException e) {
            result.rejectValue("passwordconfirm", "account.passwordconfirm", e.getMessage());
        }

        return "account";
    }

    @GetMapping("accountConfirm")
    public String confirmAccount(String token) {

        VerificationToken verificationToken = accountService.findByToken(token);

        // Check that the verification token expiry date is not in the past
        if(verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            accountService.deleteAccount(verificationToken.getTokenId().getUsername());
            return "accountExpired";
        }

        accountService.confirmUserAccount(verificationToken.getTokenId().getUsername());
        return "accountConfirmed";
    }
}
