package org.olivetree.foodrecipesspring.controller;

import org.olivetree.foodrecipesspring.model.RecipeDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class RecipesController {
    @GetMapping("recipes")
    public String getRecipes(@ModelAttribute("recipe") RecipeDto recipe) {
        return "recipes";
    }
}
