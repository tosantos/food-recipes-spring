package org.olivetree.foodrecipesspring.events;

import org.olivetree.foodrecipesspring.model.UserAccountDto;
import org.olivetree.foodrecipesspring.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PasswordListener implements ApplicationListener<OnResetPasswordEvent> {
    @Value("${recipeapp.server.url}")
    private String serverUrl;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnResetPasswordEvent event) {
        UserAccountDto userAccountDto = event.getUserAccountDto();

        String token = UUID.randomUUID().toString();

        accountService.createPasswordResetToken(userAccountDto.username(), token);

        String confirmationUrl = "/passwordReset?token=" + token;

        // Send email
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(userAccountDto.email());
        email.setSubject("Reset Account Password");
        email.setText("Reset Password: " + "\r\n" + serverUrl + confirmationUrl);
        mailSender.send(email);
    }
}
