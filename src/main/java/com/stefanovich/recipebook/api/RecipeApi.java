package com.stefanovich.recipebook.api;

import com.stefanovich.recipebook.model.Recipe;
import com.stefanovich.recipebook.model.dto.RecipeDTO;
import com.stefanovich.recipebook.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/recipes")
@RequiredArgsConstructor
public class RecipeApi implements RecipeApiSwagger {
    private final RecipeService recipeService;
    private static final Logger logger = LoggerFactory.getLogger(RecipeApi.class);


    // in advice error(with stacktrace) or warn log

    /**
     * Adds a new recipe.
     *
     * @param recipeDTO The data transfer object containing the details of the recipe.
     * @return A ResponseEntity containing the added recipe as a data transfer object.
     */
    @PostMapping
    public ResponseEntity<RecipeDTO> addRecipe(@Valid @RequestBody RecipeDTO recipeDTO) {
        logger.info("Adding new recipe: {}", recipeDTO);
        RecipeDTO addedRecipe = recipeService.addRecipe(recipeDTO);
        logger.debug("Added recipe: {}", addedRecipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedRecipe);
    }

    /**
     * Updates an existing recipe.
     *
     * @param recipeId      The ID of the recipe to update.
     * @param updatedRecipe The data transfer object containing the updated details of the recipe.
     * @return A ResponseEntity containing the updated recipe as a data transfer object.
     */
    @PutMapping("/{recipeId}")
    public ResponseEntity<RecipeDTO> updateRecipe(@Valid @PathVariable Long recipeId, @RequestBody RecipeDTO updatedRecipe) {
        logger.info("Updating recipe with id {}: {}", recipeId, updatedRecipe);
        RecipeDTO updated = recipeService.updateRecipe(recipeId, updatedRecipe);
        logger.debug("Updated recipe: {}", updated);
        return ResponseEntity.ok(updated);
    }

    /**
     * Retrieves all recipes.
     *
     * @return A list of all recipes as data transfer objects.
     */
    @GetMapping
    public List<RecipeDTO> getAllRecipes() {
        logger.info("Getting all recipes");
        return recipeService.getAllRecipes();
    }

    /**
     * Filters recipes based on various criteria.
     *
     * @param vegetarian         Whether the recipes should be vegetarian.
     * @param servings           The number of servings the recipes should have.
     * @param includeIngredients The ingredients the recipes should include.
     * @param excludeIngredients The ingredients the recipes should exclude.
     * @param searchText         The text to search for in the recipes.
     * @return A ResponseEntity containing a list of recipes that match the given criteria.
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Recipe>> filterRecipes(
            @RequestParam(name = "vegetarian", required = false) Boolean vegetarian,
            @RequestParam(name = "servings", required = false) Integer servings,
            @RequestParam(name = "includeIngredients", required = false) List<String> includeIngredients,
            @RequestParam(name = "excludeIngredients", required = false) List<String> excludeIngredients,
            @RequestParam(name = "searchText", required = false) String searchText
    ) {
        logger.info("Filtering recipes with parameters - vegetarian: {}, servings: {}, includeIngredients: {}, excludeIngredients: {}, searchText: {}",
                vegetarian, servings, includeIngredients, excludeIngredients, searchText);
        List<Recipe> filteredRecipes = recipeService.filterRecipes(vegetarian, servings, includeIngredients, excludeIngredients, searchText);
        logger.debug("Filtered recipes: {}", filteredRecipes);
        return ResponseEntity.ok(filteredRecipes);
    }

    /**
     * Deletes a recipe.
     *
     * @param recipeId The ID of the recipe to delete.
     * @return A ResponseEntity indicating that the recipe has been deleted.
     */
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long recipeId) {
        logger.info("Delete recipe with id {}: ", recipeId);
        recipeService.deleteRecipe(recipeId);
        logger.debug("Deleted recipe with id: {}", recipeId);
        return ResponseEntity.noContent().build();

    }
}
