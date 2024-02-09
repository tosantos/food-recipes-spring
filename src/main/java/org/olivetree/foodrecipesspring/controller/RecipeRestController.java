package org.olivetree.foodrecipesspring.controller;

import org.olivetree.foodrecipesspring.model.RecipeDto;
import org.olivetree.foodrecipesspring.model.RecipeFilterDto;
import org.olivetree.foodrecipesspring.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeRestController {

    @Autowired
    private RecipeService recipeService;

    @PostMapping
    public ResponseEntity<RecipeDto> createRecipe(@RequestBody final RecipeDto recipe) {
        RecipeDto createdRecipe = recipeService.createRecipe(recipe);
        return new ResponseEntity<>(createdRecipe, HttpStatus.CREATED);
    }

    @RequestMapping(value="{id}", method = RequestMethod.PUT)
    public ResponseEntity<RecipeDto> updateRecipe(@PathVariable Long id, @RequestBody RecipeDto recipe) {
        RecipeDto recipeDto = recipeService.updateRecipe(id, recipe);
        return new ResponseEntity<>(recipeDto, HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("{id}")
    public ResponseEntity<RecipeDto> get(@PathVariable Long id) {
        RecipeDto recipe = recipeService.getRecipeById(id);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping
    public ResponseEntity<List<RecipeDto>> getRecipes(RecipeFilterDto filter) {
        List<RecipeDto> recipes = recipeService.getRecipes(filter);
        return ResponseEntity.ok(recipes);
    }

    /*


    @RequestMapping(value="{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        recipeRepository.deleteById(id);
    }
     */
}
