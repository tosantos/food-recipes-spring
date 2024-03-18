package org.olivetree.foodrecipesspring.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PasswordDto(@NotNull @NotEmpty String username,
                          @NotNull @NotEmpty String password,
                          @NotNull @NotEmpty String matchingPassword) {
}
