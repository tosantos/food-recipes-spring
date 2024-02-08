package org.olivetree.foodrecipesspring.model;

import java.time.LocalDateTime;

public record RecipeDto(Long id, String name, String description, Long duration, LocalDateTime created) {
}
