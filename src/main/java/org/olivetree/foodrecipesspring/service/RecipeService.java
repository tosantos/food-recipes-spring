package org.olivetree.foodrecipesspring.service;

import org.olivetree.foodrecipesspring.model.RecipeDto;
import org.olivetree.foodrecipesspring.model.RecipeFilterDto;

import java.util.List;

public interface RecipeService {
    RecipeDto createRecipe(RecipeDto recipeDto);

    void deleteRecipe(Long recipeId);

    RecipeDto updateRecipe(Long recipeId, RecipeDto recipeDto);

    RecipeDto getRecipeById(Long recipeId);

    List<RecipeDto> getRecipes(RecipeFilterDto recipeFilterDto);
}
