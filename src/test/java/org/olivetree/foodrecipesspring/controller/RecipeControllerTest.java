package org.olivetree.foodrecipesspring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.olivetree.foodrecipesspring.exception.RecipeNotFoundException;
import org.olivetree.foodrecipesspring.model.RecipeDto;
import org.olivetree.foodrecipesspring.model.RecipeFilterDto;
import org.olivetree.foodrecipesspring.service.RecipeService;
import org.olivetree.foodrecipesspring.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RecipeService recipeService;

    @Test
    @DisplayName("Given recipe details, when recipe is created then recipe is saved")
    void givenRecipeDetailsWhenRecipeIsCreatedThenRecipeIsSaved() throws Exception {
        String description = "Sample Recipe Description";
        String name = "Sample Recipe Name";
        RecipeDto recipeDto = new RecipeDto(null, name, description, 30L, null);

        when(recipeService.createRecipe(any(RecipeDto.class))).thenReturn(recipeDto);

        mockMvc.perform(post("/api/v1/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.description", is(description)));
    }

    @Test
    @DisplayName("Given an existing recipe, when recipe is updated then recipe details are updated")
    void givenAnExistingRecipeWhenRecipeIsUpdatedThenRecipeDetailsAreUpdated() throws Exception {

        Long recipeId = 1L;
        String description = "Sample Recipe Description";
        String name = "Sample Recipe Name";
        RecipeDto recipeDto = new RecipeDto(null, name, description, 30L, null);

        when(recipeService.updateRecipe(eq(recipeId), any(RecipeDto.class)))
                .thenReturn(recipeDto);

        mockMvc.perform(put("/api/v1/recipes/{id}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.description", is(description)));
    }

    @Test
    @DisplayName("Given an non-existing recipe id, when recipe is updated then a RecipeNotFoundException is thrown")
    void givenAnNonExistingRecipeIdWhenRecipeIsUpdatedThenAnExceptionIsThrown() throws Exception {
        Long recipeId = 9999L;

        String description = "Sample Recipe Description";
        String name = "Sample Recipe Name";
        RecipeDto recipeDto = new RecipeDto(null, name, description, 30L, null);

        when(recipeService.updateRecipe(eq(recipeId), any(RecipeDto.class)))
                .thenThrow(new RecipeNotFoundException(ErrorMessages.RECIPE_NOT_FOUND));

        mockMvc.perform(put("/api/v1/recipes/{id}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessages.RECIPE_NOT_FOUND));
    }

    @Test
    @DisplayName("Given valid recipe ID, when getting the recipe then return recipe details")
    void givenAnValidIdWhenGettingRecipeThenReturnRecipeDetails() throws Exception {
        Long recipeId = 1L;
        String description = "Sample Recipe Description";
        String name = "Sample Recipe Name";
        RecipeDto recipeDto = new RecipeDto(recipeId, name, description, 30L, LocalDateTime.now());

        when(recipeService.getRecipeById(eq(recipeId)))
                .thenReturn(recipeDto);

        mockMvc.perform(get("/api/v1/recipes/{id}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(recipeId.intValue())))
                .andExpect(jsonPath("$.description", is(description)))
                .andExpect(jsonPath("$.name", is(name)));
    }

    @Test
    @DisplayName("Given an non-existing recipe ID, when getting the recipe then a RecipeNotFoundException is thrown")
    void givenAnNonExistingRecipeIdWhenGettingRecipeThenAnExceptionIsThrown() throws Exception {
        Long recipeId = 9999L;

        when(recipeService.getRecipeById(eq(recipeId)))
                .thenThrow(new RecipeNotFoundException(ErrorMessages.RECIPE_NOT_FOUND));

        mockMvc.perform(get("/api/v1/recipes/{id}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessages.RECIPE_NOT_FOUND));
    }

    @Test
    @DisplayName("Given filtered criteria, when getting the recipes then return filtered recipes")
    void givenFilteredCriteriaWhenGettingRecipesThenReturnFilteredRecipes() throws Exception {
        String description = "Sample Recipe Description";
        String name = "Sample Recipe Name";
        RecipeDto recipeDto1 = new RecipeDto(1L, name, description, 30L, LocalDateTime.now());
        RecipeDto recipeDto2 = new RecipeDto(2L, name, description, 30L, LocalDateTime.now());

        List<RecipeDto> filteredRecipes = List.of(recipeDto1, recipeDto2);

        when(recipeService.getRecipes(any(RecipeFilterDto.class)))
                .thenReturn(filteredRecipes);

        mockMvc.perform(get("/api/v1/recipes")
                        .param("duration", "30")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(filteredRecipes.size())))
                .andExpect(jsonPath("$[0].id", is(recipeDto1.id().intValue())))
                .andExpect(jsonPath("$[1].id", is(recipeDto2.id().intValue())));
    }
}