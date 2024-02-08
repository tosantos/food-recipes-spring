package org.olivetree.foodrecipesspring.repository;

import org.olivetree.foodrecipesspring.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
