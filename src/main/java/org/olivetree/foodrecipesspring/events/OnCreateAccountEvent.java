package org.olivetree.foodrecipesspring.events;

import org.olivetree.foodrecipesspring.domain.Account;
import org.springframework.context.ApplicationEvent;

public class OnCreateAccountEvent extends ApplicationEvent {
    public OnCreateAccountEvent(Account account) {
        super(account);
    }

    public Account getAccount() {
        return (Account) getSource();
    }
}
