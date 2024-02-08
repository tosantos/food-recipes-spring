package org.olivetree.foodrecipesspring.exception;

public class MissingNameInRecipeException extends RuntimeException {
    public MissingNameInRecipeException(String msg) {
        super(msg);
    }
}
