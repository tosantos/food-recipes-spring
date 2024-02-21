package org.olivetree.foodrecipesspring.exception;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String msg) {
        super(msg);
    }
}
