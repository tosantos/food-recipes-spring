package org.olivetree.foodrecipesspring.events;

import org.olivetree.foodrecipesspring.domain.Account;
import org.olivetree.foodrecipesspring.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountListener implements ApplicationListener<OnCreateAccountEvent> {

    @Value("${recipeapp.server.url}")
    private String serverUrl;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AccountService accountService;

    @Override
    public void onApplicationEvent(OnCreateAccountEvent event) {
        Account account = event.getAccount();
        String token = UUID.randomUUID().toString();
        accountService.createVerificationToken(token, account);

        String confirmationUrl = "/accountConfirm?token=" + token;

        // Send email
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(account.getEmail());
        email.setSubject("Recipe Book Account Confirmation");
        email.setText("Please confirm your account: " + "\r\n" + serverUrl + confirmationUrl);
        mailSender.send(email);
    }
}
