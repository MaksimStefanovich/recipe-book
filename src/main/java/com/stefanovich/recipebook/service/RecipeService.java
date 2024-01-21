package com.stefanovich.recipebook.service;

import com.stefanovich.recipebook.exception.RecipeNotFoundException;
import com.stefanovich.recipebook.model.Difficulty;
import com.stefanovich.recipebook.model.Ingredient;
import com.stefanovich.recipebook.model.IngredientInRecipe;
import com.stefanovich.recipebook.model.Recipe;
import com.stefanovich.recipebook.model.dto.IngredientDTO;
import com.stefanovich.recipebook.model.dto.IngredientInRecipeDTO;
import com.stefanovich.recipebook.model.dto.RecipeDTO;
import com.stefanovich.recipebook.repository.IngredientInRecipeRepository;
import com.stefanovich.recipebook.repository.IngredientRepository;
import com.stefanovich.recipebook.repository.RecipeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientInRecipeRepository ingredientInRecipeRepository;
    private final IngredientRepository ingredientRepository;
    private final EntityManager entityManager;

    /**
     * Adds a new recipe to the repository.
     *
     * @param recipeDTO The data transfer object containing the details of the recipe.
     * @return The added recipe as a data transfer object.
     */
    public RecipeDTO addRecipe(RecipeDTO recipeDTO) {
        Recipe recipe = convertDtoToRecipe(recipeDTO);
        Recipe savedRecipe = recipeRepository.save(recipe);
        addIngredientsToRecipe(savedRecipe, recipeDTO.getIngredients());
        return convertToDTO(savedRecipe);
    }

    /**
     * Converts a RecipeDTO object to a Recipe entity.
     *
     * @param recipeDTO The data transfer object to convert.
     * @return The converted Recipe entity.
     */
    private Recipe convertDtoToRecipe(RecipeDTO recipeDTO) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeDTO.getName());
        recipe.setInstructions(recipeDTO.getInstructions());
        recipe.setServings(recipeDTO.getServings());
        recipe.setDifficulty(Difficulty.valueOf(recipeDTO.getDifficulty()));
        recipe.setVegetarian(recipeDTO.isVegetarian());
        recipe.setPreparationTime(recipeDTO.getPreparationTime());
        return recipe;
    }

    /**
     * Adds ingredients to a recipe.
     *
     * @param recipe                 The recipe to which the ingredients should be added.
     * @param ingredientInRecipeDTOs The ingredients to add.
     */
    private void addIngredientsToRecipe(Recipe recipe, List<IngredientInRecipeDTO> ingredientInRecipeDTOs) {
        for (IngredientInRecipeDTO ingredientInRecipeDTO : ingredientInRecipeDTOs) {
            IngredientDTO ingredientDTO = ingredientInRecipeDTO.getIngredient();
            Ingredient ingredient = getOrCreateIngredient(ingredientDTO);
            IngredientInRecipe ingredientInRecipe = createIngredientInRecipe(ingredient, recipe, ingredientInRecipeDTO);
            ingredientInRecipeRepository.save(ingredientInRecipe);
            recipe.getIngredients().add(ingredientInRecipe);
        }
    }

    /**
     * Retrieves an existing ingredient or creates a new one if it doesn't exist.
     *
     * @param ingredientDTO The data transfer object containing the details of the ingredient.
     * @return The retrieved or newly created ingredient.
     */
    private Ingredient getOrCreateIngredient(IngredientDTO ingredientDTO) {
        Optional<Ingredient> existingIngredient = ingredientRepository.findByName(ingredientDTO.getName());
        if (existingIngredient.isPresent()) {
            return existingIngredient.get();
        } else {
            Ingredient newIngredient = new Ingredient();
            newIngredient.setName(ingredientDTO.getName());
            return ingredientRepository.save(newIngredient);
        }
    }

    /**
     * Creates a new IngredientInRecipe entity.
     *
     * @param ingredient            The ingredient to add.
     * @param recipe                The recipe to which the ingredient should be added.
     * @param ingredientInRecipeDTO The data transfer object containing the details of the ingredient in the recipe.
     * @return The created IngredientInRecipe entity.
     */
    private IngredientInRecipe createIngredientInRecipe(Ingredient ingredient, Recipe recipe, IngredientInRecipeDTO ingredientInRecipeDTO) {
        IngredientInRecipe ingredientInRecipe = new IngredientInRecipe();
        ingredientInRecipe.setIngredient(ingredient);
        ingredientInRecipe.setRecipe(recipe);
        ingredientInRecipe.setQuantity(ingredientInRecipeDTO.getQuantity());
        ingredientInRecipe.setUnitOfMeasure(ingredientInRecipeDTO.getUnitOfMeasure());
        return ingredientInRecipe;
    }

    /**
     * Updates an existing recipe in the repository.
     *
     * @param recipeId      The ID of the recipe to update.
     * @param updatedRecipe The data transfer object containing the updated details of the recipe.
     * @return The updated recipe as a data transfer object.
     */
    public RecipeDTO updateRecipe(Long recipeId, RecipeDTO updatedRecipe) {
        Recipe recipe = getRecipeById(recipeId);
        updateRecipeDetails(recipe, updatedRecipe);
        Map<String, IngredientInRecipe> existingIngredients = getExistingIngredients(recipe);
        updateRecipeIngredients(recipe, updatedRecipe, existingIngredients);
        recipeRepository.save(recipe);
        return convertToDTO(recipe);
    }

    /**
     * Retrieves a recipe by its ID.
     *
     * @param recipeId The ID of the recipe to retrieve.
     * @return The retrieved recipe.
     * @throws RecipeNotFoundException if no recipe is found with the given ID.
     */
    private Recipe getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Not found recipe with id - " + recipeId));
    }

    /**
     * Updates the details of a recipe.
     *
     * @param recipe        The recipe to update.
     * @param updatedRecipe The data transfer object containing the updated details of the recipe.
     */
    private void updateRecipeDetails(Recipe recipe, RecipeDTO updatedRecipe) {
        recipe.setName(updatedRecipe.getName());
        recipe.setInstructions(updatedRecipe.getInstructions());
        recipe.setPreparationTime(updatedRecipe.getPreparationTime());
        recipe.setDifficulty(Difficulty.valueOf(updatedRecipe.getDifficulty()));
    }

    /**
     * Retrieves the existing ingredients of a recipe.
     *
     * @param recipe The recipe whose ingredients should be retrieved.
     * @return A map of ingredient names to IngredientInRecipe entities.
     */
    private Map<String, IngredientInRecipe> getExistingIngredients(Recipe recipe) {
        Map<String, IngredientInRecipe> existingIngredients = new HashMap<>();
        for (IngredientInRecipe ingredientInRecipe : recipe.getIngredients()) {
            existingIngredients.put(ingredientInRecipe.getIngredient().getName(), ingredientInRecipe);
        }
        return existingIngredients;
    }

    /**
     * Updates the ingredients of a recipe.
     *
     * @param recipe              The recipe whose ingredients should be updated.
     * @param updatedRecipe       The data transfer object containing the updated details of the recipe.
     * @param existingIngredients A map of existing ingredient names to IngredientInRecipe entities.
     */
    private void updateRecipeIngredients(Recipe recipe, RecipeDTO updatedRecipe, Map<String, IngredientInRecipe> existingIngredients) {
        for (IngredientInRecipeDTO ingredientInRecipeDTO : updatedRecipe.getIngredients()) {
            IngredientDTO ingredientDTO = ingredientInRecipeDTO.getIngredient();
            Ingredient ingredient = getOrCreateIngredient(ingredientDTO);

            if (existingIngredients.containsKey(ingredient.getName())) {
                updateExistingIngredient(existingIngredients, ingredientInRecipeDTO, ingredient);
            } else {
                addNewIngredient(recipe, ingredientInRecipeDTO, ingredient);
            }
        }
    }

    /**
     * Updates an existing ingredient in a recipe.
     *
     * @param existingIngredients   A map of existing ingredient names to IngredientInRecipe entities.
     * @param ingredientInRecipeDTO The data transfer object containing the updated details of the ingredient in the recipe.
     * @param ingredient            The ingredient to update.
     */
    private void updateExistingIngredient(Map<String, IngredientInRecipe> existingIngredients, IngredientInRecipeDTO ingredientInRecipeDTO, Ingredient ingredient) {
        IngredientInRecipe existingIngredientInRecipe = existingIngredients.get(ingredient.getName());
        existingIngredientInRecipe.setQuantity(ingredientInRecipeDTO.getQuantity());
        existingIngredientInRecipe.setUnitOfMeasure(ingredientInRecipeDTO.getUnitOfMeasure());
    }

    /**
     * Adds a new ingredient to a recipe.
     *
     * @param recipe                The recipe to which the ingredient should be added.
     * @param ingredientInRecipeDTO The data transfer object containing the details of the ingredient in the recipe.
     * @param ingredient            The ingredient to add.
     */
    private void addNewIngredient(Recipe recipe, IngredientInRecipeDTO ingredientInRecipeDTO, Ingredient ingredient) {
        IngredientInRecipe ingredientInRecipe = createIngredientInRecipe(ingredient, recipe, ingredientInRecipeDTO);
        ingredientInRecipeRepository.save(ingredientInRecipe);
        recipe.getIngredients().add(ingredientInRecipe);
    }

    /**
     * Retrieves all recipes.
     *
     * @return A list of all recipes as data transfer objects.
     */
    public List<RecipeDTO> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Filters recipes based on various criteria.
     *
     * @param vegetarian         Whether the recipes should be vegetarian.
     * @param servings           The number of servings the recipes should have.
     * @param includeIngredients The ingredients the recipes should include.
     * @param excludeIngredients The ingredients the recipes should exclude.
     * @param searchText         The text to search for in the recipes.
     * @return A list of recipes that match the given criteria.
     */
    public List<Recipe> filterRecipes(
            Boolean vegetarian,
            Integer servings,
            List<String> includeIngredients,
            List<String> excludeIngredients,
            String searchText
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Recipe> criteriaQuery = criteriaBuilder.createQuery(Recipe.class);
        Root<Recipe> root = criteriaQuery.from(Recipe.class);

        // Create a list to hold the conditions
        Predicate[] predicates = buildPredicates(criteriaBuilder, criteriaQuery, root, vegetarian, servings, includeIngredients, excludeIngredients, searchText);

        // Combine all predicates with AND
        criteriaQuery.where(predicates);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * Builds predicates for a criteria query based on various criteria.
     *
     * @param criteriaBuilder    The CriteriaBuilder to use to build the predicates.
     * @param criteriaQuery      The CriteriaQuery to which the predicates should be added.
     * @param root               The root of the query.
     * @param vegetarian         Whether the recipes should be vegetarian.
     * @param servings           The number of servings the recipes should have.
     * @param includeIngredients The ingredients the recipes should include.
     * @param excludeIngredients The ingredients the recipes should exclude.
     * @param searchText         The text to search for in the recipes.
     * @return An array of predicates for the criteria query.
     */
    private Predicate[] buildPredicates(
            CriteriaBuilder criteriaBuilder,
            CriteriaQuery<?> criteriaQuery,
            Root<Recipe> root,
            Boolean vegetarian,
            Integer servings,
            List<String> includeIngredients,
            List<String> excludeIngredients,
            String searchText
    ) {
        // Create a list to hold the conditions
        List<Predicate> predicates = new ArrayList<>();

        // Add criteria based on the provided parameters
        if (vegetarian != null) {
            predicates.add(criteriaBuilder.equal(root.get("vegetarian"), vegetarian));
        }

        if (servings != null) {
            predicates.add(criteriaBuilder.equal(root.get("servings"), servings));
        }

        if (includeIngredients != null && !includeIngredients.isEmpty()) {
            Join<Recipe, IngredientInRecipe> ingredientsJoin = root.join("ingredients");
            Join<IngredientInRecipe, Ingredient> ingredientJoin = ingredientsJoin.join("ingredient");
            Predicate ingredientPredicate = ingredientJoin.get("name").in(includeIngredients);
            predicates.add(ingredientPredicate);
        }

        if (excludeIngredients != null && !excludeIngredients.isEmpty()) {
            Subquery<IngredientInRecipe> sq = criteriaQuery.subquery(IngredientInRecipe.class);
            Root<IngredientInRecipe> ingredientInRecipe = sq.from(IngredientInRecipe.class);
            sq.select(ingredientInRecipe)
                    .where(
                            criteriaBuilder.and(
                                    criteriaBuilder.equal(ingredientInRecipe.get("recipe"), root),
                                    ingredientInRecipe.get("ingredient").get("name").in(excludeIngredients)
                            )
                    );
            predicates.add(criteriaBuilder.not(criteriaBuilder.exists(sq)));
        }

        if (searchText != null && !searchText.isEmpty()) {
            Predicate namePredicate = criteriaBuilder.like(root.get("name"), "%" + searchText + "%");
            Predicate instructionsPredicate = criteriaBuilder.like(root.get("instructions"), "%" + searchText + "%");
            Join<Recipe, IngredientInRecipe> ingredientsJoin = root.join("ingredients");
            Join<IngredientInRecipe, Ingredient> ingredientJoin = ingredientsJoin.join("ingredient");
            Predicate ingredientPredicate = criteriaBuilder.like(ingredientJoin.get("name"), "%" + searchText + "%");
            predicates.add(criteriaBuilder.or(namePredicate, instructionsPredicate, ingredientPredicate));
        }

        return predicates.toArray(new Predicate[0]);
    }

    /**
     * Deletes a recipe from the repository.
     *
     * @param recipeId The ID of the recipe to delete.
     * @throws RecipeNotFoundException if no recipe is found with the given ID.
     */
    public void deleteRecipe(Long recipeId) {
        // Get the recipe by id
        Recipe recipeToDelete = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Not found recipe with id - " + recipeId));

        // Delete the recipe
        recipeRepository.delete(recipeToDelete);
    }

    /**
     * Converts a Recipe entity to a RecipeDTO object.
     *
     * @param recipe The Recipe entity to convert.
     * @return The converted RecipeDTO object.
     */
    private RecipeDTO convertToDTO(Recipe recipe) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(recipe, RecipeDTO.class);
    }

}

