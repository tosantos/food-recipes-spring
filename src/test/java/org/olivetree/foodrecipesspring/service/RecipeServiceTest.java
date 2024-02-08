package org.olivetree.foodrecipesspring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.olivetree.foodrecipesspring.domain.Recipe;
import org.olivetree.foodrecipesspring.exception.MissingNameInRecipeException;
import org.olivetree.foodrecipesspring.exception.RecipeNotFoundException;
import org.olivetree.foodrecipesspring.model.RecipeDto;
import org.olivetree.foodrecipesspring.repository.RecipeRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {
    private RecipeService recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @BeforeEach
    void setup() {
        recipeService = new RecipeServiceImpl(recipeRepository);
    }

    @Test
    @DisplayName("Given Recipe details, when recipe is created then sets creation date")
    void givenRecipeDetailsWhenRecipeIsCreatedThenSetsCreationDate() {
        String recipeName = "Name";
        String recipeDescription = "Description";

        RecipeDto recipeDto = new RecipeDto(null, recipeName, recipeDescription, 30L, null);

        Recipe recipe = getRecipeFromRepository(1L);

        when(recipeRepository.saveAndFlush(any(Recipe.class)))
                .thenReturn(recipe);

        RecipeDto createdRecipe = recipeService.createRecipe(recipeDto);

        verify(recipeRepository, times(1)).saveAndFlush(any(Recipe.class));

        assertNotNull(createdRecipe);
        assertNotNull(createdRecipe.created());
        assertEquals(recipeName, createdRecipe.name());
        assertEquals(recipeDescription, createdRecipe.description());
    }

    @Test
    @DisplayName("Given Recipe without any name, when recipe is created then a MissingNameInRecipeException is thrown")
    void givenRecipeWithoutNameWhenRecipeIsCreatedThenThrowException() {
        RecipeDto recipeDto = new RecipeDto(null, null, "Description", 30L, null);

        assertThrows(MissingNameInRecipeException.class, () -> recipeService.createRecipe(recipeDto));
    }

    @Test
    @DisplayName("Given Recipe details, when recipe is updated then updates recipe details")
    void givenRecipeDetailsWhenRecipeIsUpdatedThenRepositorySaveAndFlushIsCalled() {
        Long recipeId = 1L;
        String updatedRecipeName = "New Recipe Name";
        String updatedRecipeDescription = "New Recipe Description";

        RecipeDto recipeDto = new RecipeDto(null, updatedRecipeName, updatedRecipeDescription, 30L, null);

        Recipe recipe = getRecipeFromRepository(recipeId);

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        when(recipeRepository.saveAndFlush(any(Recipe.class)))
                .thenReturn(recipe);

        RecipeDto updatedRecipe = recipeService.updateRecipe(recipeId, recipeDto);

        verify(recipeRepository, times(1)).saveAndFlush(any(Recipe.class));

        assertNotNull(updatedRecipe);
        assertEquals(updatedRecipeName, updatedRecipe.name());
        assertEquals(updatedRecipeDescription, updatedRecipe.description());
    }

    @Test
    @DisplayName("Given non-existing recipe id, when recipe is updated then a RecipeNotFoundException is thrown")
    void givenNonExistingRecipeIdWhenRecipeIsUpdatedThenExceptionThrown() {
        Long nonExistingRecipeId = 9999L;

        String updatedRecipeName = "New Recipe Name";
        String updatedRecipeDescription = "New Recipe Description";

        RecipeDto recipeDto = new RecipeDto(null, updatedRecipeName, updatedRecipeDescription, 30L, null);

        when(recipeRepository.findById(nonExistingRecipeId)).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(nonExistingRecipeId, recipeDto));
    }

    @Test
    @DisplayName("Given an existing recipe id, when recipe is retrieved then should get recipe details")
    void givenExistingRecipeIdWhenRecipeIsRetrievedThenGetRecipeDetails() {
        Long recipeId = 1L;

        Recipe recipe = getRecipeFromRepository(recipeId);

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        RecipeDto recipeDto = recipeService.getRecipeById(recipeId);
        assertNotNull(recipeDto);
        assertEquals(recipe.getName(), recipeDto.name());
        assertEquals(recipe.getDescription(), recipeDto.description());
        assertEquals(recipe.getDuration(), recipeDto.duration());
    }

    @Test
    @DisplayName("Given non-existing recipe id, when recipe is retrieved then a RecipeNotFoundException should be thrown")
    void givenNonExistingRecipeIdWhenRecipeIsRetrievedThenExceptionThrownGetRecipeDetails() {
        Long nonExistingRecipeId = 9999L;

        when(recipeRepository.findById(nonExistingRecipeId))
                .thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipeById(nonExistingRecipeId));
    }

    private static Recipe getRecipeFromRepository(Long recipeId) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        recipe.setDuration(30L);
        recipe.setName("Name");
        recipe.setDescription("Description");
        recipe.setCreated(LocalDateTime.now());
        return recipe;
    }
}