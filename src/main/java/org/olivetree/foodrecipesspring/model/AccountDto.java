package org.olivetree.foodrecipesspring.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AccountDto(@NotNull @NotEmpty String username,
                         @NotNull @NotEmpty String firstname,
                         @NotNull @NotEmpty String lastname,
                         @NotNull @NotEmpty String password,
                         @NotNull @NotEmpty String passwordconfirm,
                         @NotNull @NotEmpty @Email String email) {}
