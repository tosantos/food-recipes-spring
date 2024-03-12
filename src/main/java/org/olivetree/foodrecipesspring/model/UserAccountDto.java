package org.olivetree.foodrecipesspring.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserAccountDto(@NotNull @NotEmpty @Email String email, String username) {
}
