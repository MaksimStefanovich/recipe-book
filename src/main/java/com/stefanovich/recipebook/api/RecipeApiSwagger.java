package com.stefanovich.recipebook.api;

import com.stefanovich.recipebook.model.Recipe;
import com.stefanovich.recipebook.model.dto.RecipeDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface RecipeApiSwagger {
    @Operation(
            summary = "Add a new recipe",
            description = "This operation creates a new recipe and saves it to the database. It takes a RecipeDTO object as input, which should contain the details of the recipe to be added. The operation returns the added recipe as a RecipeDTO object."
    )
    ResponseEntity<RecipeDTO> addRecipe(@Valid @RequestBody RecipeDTO recipeDTO);

    @Operation(
            summary = "Update an existing recipe",
            description = "This operation updates an existing recipe in the database. It takes the ID of the recipe to be updated and a RecipeDTO object containing the updated details. The operation returns the updated recipe as a RecipeDTO object."
    )
    ResponseEntity<RecipeDTO> updateRecipe(@PathVariable Long recipeId, @RequestBody RecipeDTO updatedRecipe);

    @Operation(
            summary = "Get all recipes",
            description = "This operation retrieves all recipes from the database and returns them as a list of RecipeDTO objects."
    )
    List<RecipeDTO> getAllRecipes();

    @Operation(
            summary = "Filter recipes based on criteria",
            description = "This operation filters recipes based on various criteria such as whether they are vegetarian, the number of servings they have, the ingredients they include or exclude, and a search text. It returns a list of recipes that match the given criteria."
    )
    ResponseEntity<List<Recipe>> filterRecipes(
            @RequestParam(name = "vegetarian", required = false) Boolean vegetarian,
            @RequestParam(name = "servings", required = false) Integer servings,
            @RequestParam(name = "includeIngredients", required = false) List<String> includeIngredients,
            @RequestParam(name = "excludeIngredients", required = false) List<String> excludeIngredients,
            @RequestParam(name = "searchText", required = false) String searchText
    );

    @Operation(
            summary = "Delete a recipe by ID",
            description = "This operation deletes a recipe from the database. It takes the ID of the recipe to be deleted. The operation does not return any content."
    )
    ResponseEntity<Void> deleteRecipe(@PathVariable Long recipeId);

}
