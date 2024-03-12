package org.olivetree.foodrecipesspring.events;

import org.olivetree.foodrecipesspring.model.UserAccountDto;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PasswordListener implements ApplicationListener<OnResetPasswordEvent> {
    @Override
    public void onApplicationEvent(OnResetPasswordEvent event) {
        UserAccountDto userAccountDto = event.getUserAccountDto();

        // TODO: Implement sending email to user
        System.out.println("Sending email to the user");
    }
}
