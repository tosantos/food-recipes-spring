package org.olivetree.foodrecipesspring.events;

import org.olivetree.foodrecipesspring.model.UserAccountDto;
import org.springframework.context.ApplicationEvent;

public class OnResetPasswordEvent extends ApplicationEvent {
    public OnResetPasswordEvent(UserAccountDto userAccountDto) {
        super(userAccountDto);
    }

    public UserAccountDto getUserAccountDto() {
        return (UserAccountDto) getSource();
    }
}
