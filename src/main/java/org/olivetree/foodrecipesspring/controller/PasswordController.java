package org.olivetree.foodrecipesspring.controller;

import jakarta.validation.Valid;
import org.olivetree.foodrecipesspring.domain.ResetToken;
import org.olivetree.foodrecipesspring.exception.PasswordMismatchException;
import org.olivetree.foodrecipesspring.model.PasswordDto;
import org.olivetree.foodrecipesspring.model.UserAccountDto;
import org.olivetree.foodrecipesspring.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

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

    @GetMapping("passwordReset")
    public ModelAndView getNewPassword(@RequestParam("token") String token){

        ResetToken resetToken = accountService.findByResetToken(token);

        // Check that the reset token is valid and expiry date is not in the past
        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return new ModelAndView("tokenExpired");
        }

        PasswordDto passwordDto = new PasswordDto(resetToken.getTokenId().getUsername(), null, null);
        return new ModelAndView("resetPassword", "password", passwordDto);
    }

    @PostMapping("passwordReset")
    public String saveNewPassword(@Valid @ModelAttribute("password") PasswordDto password,
                                  BindingResult result) {

        // Check for errors
        if(result.hasErrors()) {
            return "resetPassword";
        }

        try {
            accountService.resetAccountPassword(password);
            return "tokenConfirmed";
        } catch (PasswordMismatchException e) {
            result.rejectValue("matchingPassword", "password.matchingPassword", e.getMessage());
            return "resetPassword";
        }
    }
}
