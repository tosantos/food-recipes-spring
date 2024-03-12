package org.olivetree.foodrecipesspring.controller;

import jakarta.validation.Valid;
import org.olivetree.foodrecipesspring.domain.Account;
import org.olivetree.foodrecipesspring.model.PasswordDto;
import org.olivetree.foodrecipesspring.model.UserAccountDto;
import org.olivetree.foodrecipesspring.repository.AccountRepository;
import org.olivetree.foodrecipesspring.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class PasswordController {

    @Autowired
    private AccountService accountService;

    @GetMapping("password")
    public String getPasswordReset(@ModelAttribute("userAccount") UserAccountDto userAccountDto) {
        return "password";
    }

    @PostMapping("password")
    public String sendEmailToReset(@Valid @ModelAttribute("userAccount") UserAccountDto userAccountDto,
                                   BindingResult result) {
        // Check for errors
        if(result.hasErrors()) {
            return "password";
        }

        accountService.resetAccount(userAccountDto.email());

        return "redirect:password?sent=true";
    }
}
