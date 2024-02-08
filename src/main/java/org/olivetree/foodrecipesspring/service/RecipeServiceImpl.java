package org.olivetree.foodrecipesspring.service;

import org.olivetree.foodrecipesspring.domain.Recipe;
import org.olivetree.foodrecipesspring.exception.MissingNameInRecipeException;
import org.olivetree.foodrecipesspring.exception.RecipeNotFoundException;
import org.olivetree.foodrecipesspring.model.RecipeDto;
import org.olivetree.foodrecipesspring.model.RecipeFilterDto;
import org.olivetree.foodrecipesspring.repository.RecipeRepository;
import org.olivetree.foodrecipesspring.util.ErrorMessages;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public RecipeDto createRecipe(RecipeDto recipeDto) {
        if(recipeDto.name() == null || recipeDto.name().isEmpty()) {
            throw new MissingNameInRecipeException(ErrorMessages.NAME_REQUIRED);
        }

        Recipe recipe = getRecipe(recipeDto);
        recipe.setCreated(LocalDateTime.now());

        Recipe createdRecipe = recipeRepository.saveAndFlush(recipe);

        return convertToRecipeDto(createdRecipe);
    }

    @Override
    public void deleteRecipe(Long recipeId) {
        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException(ErrorMessages.RECIPE_NOT_FOUND));

        recipeRepository.delete(existingRecipe);
    }

    @Override
    public RecipeDto updateRecipe(Long id, RecipeDto recipeDto) {
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(ErrorMessages.RECIPE_NOT_FOUND));

        Recipe recipe = getRecipe(recipeDto);

        BeanUtils.copyProperties(recipe, existingRecipe, getNullPropertyNames(recipe));
        Recipe updatedRecipe = recipeRepository.saveAndFlush(existingRecipe);

        return convertToRecipeDto(updatedRecipe);
    }
    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @Override
    public RecipeDto getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(ErrorMessages.RECIPE_NOT_FOUND));
        return convertToRecipeDto(recipe);
    }

    @Override
    public List<RecipeDto> getRecipes(RecipeFilterDto recipeFilterDto) {
        List<Recipe> recipes = recipeRepository.findAll();

        return recipes.stream()
                .map(this::convertToRecipeDto)
                .toList();
    }

    private RecipeDto convertToRecipeDto(Recipe recipe) {
        return new RecipeDto(recipe.getId(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getDuration(),
                recipe.getCreated());
    }

    private Recipe getRecipe(RecipeDto recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setDescription(recipeDto.description());
        recipe.setName(recipeDto.name());
        recipe.setDuration(recipeDto.duration());
        recipe.setCreated(recipeDto.created());
        return recipe;
    }
}
