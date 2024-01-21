package com.stefanovich.recipebook;

import com.stefanovich.recipebook.model.Difficulty;
import com.stefanovich.recipebook.model.Recipe;
import com.stefanovich.recipebook.model.dto.RecipeDTO;
import com.stefanovich.recipebook.repository.RecipeRepository;
import com.stefanovich.recipebook.service.RecipeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @InjectMocks
    private RecipeService recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Recipe> criteriaQuery;

    @Mock
    private Root<Recipe> root;

    @Mock
    private TypedQuery<Recipe> typedQuery;

    @Test
    public void testAddRecipe() {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setName("Test Recipe");
        recipeDTO.setInstructions("Test Instructions");
        recipeDTO.setPreparationTime(30);
        recipeDTO.setServings(4);
        recipeDTO.setDifficulty("MEDIUM");
        recipeDTO.setVegetarian(false);
        recipeDTO.setIngredients(new ArrayList<>());

        Recipe recipe = new Recipe();
        recipe.setName(recipeDTO.getName());
        recipe.setInstructions(recipeDTO.getInstructions());
        recipe.setPreparationTime(recipeDTO.getPreparationTime());
        recipe.setServings(recipeDTO.getServings());
        recipe.setDifficulty(Difficulty.valueOf(recipeDTO.getDifficulty()));
        recipe.setVegetarian(recipeDTO.isVegetarian());

        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        RecipeDTO result = recipeService.addRecipe(recipeDTO);

        assertEquals(recipeDTO.getName(), result.getName());
        assertEquals(recipeDTO.getInstructions(), result.getInstructions());
        assertEquals(recipeDTO.getPreparationTime(), result.getPreparationTime());
        assertEquals(recipeDTO.getServings(), result.getServings());
        assertEquals(recipeDTO.getDifficulty(), result.getDifficulty());
        assertEquals(recipeDTO.isVegetarian(), result.isVegetarian());
    }

    @Test
    public void testUpdateRecipe() {
        Long recipeId = 1L;
        RecipeDTO updatedRecipeDTO = new RecipeDTO();
        updatedRecipeDTO.setName("Updated Recipe");
        updatedRecipeDTO.setInstructions("Updated Instructions");
        updatedRecipeDTO.setPreparationTime(35);
        updatedRecipeDTO.setServings(5);
        updatedRecipeDTO.setDifficulty("HARD");
        updatedRecipeDTO.setVegetarian(true);
        updatedRecipeDTO.setIngredients(new ArrayList<>());

        Recipe recipe = new Recipe();
        recipe.setName(updatedRecipeDTO.getName());
        recipe.setInstructions(updatedRecipeDTO.getInstructions());
        recipe.setServings(updatedRecipeDTO.getServings());
        recipe.setDifficulty(Difficulty.valueOf(updatedRecipeDTO.getDifficulty()));
        recipe.setVegetarian(updatedRecipeDTO.isVegetarian());

        when(recipeRepository.findById(any(Long.class))).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        RecipeDTO result = recipeService.updateRecipe(recipeId, updatedRecipeDTO);

        assertEquals(updatedRecipeDTO.getName(), result.getName());
        assertEquals(updatedRecipeDTO.getInstructions(), result.getInstructions());
        assertEquals(updatedRecipeDTO.getPreparationTime(), result.getPreparationTime());
        assertEquals(updatedRecipeDTO.getServings(), result.getServings());
        assertEquals(updatedRecipeDTO.getDifficulty(), result.getDifficulty());
        assertEquals(updatedRecipeDTO.isVegetarian(), result.isVegetarian());
    }

    @Test
    public void testGetAllRecipes() {
        Recipe recipe1 = new Recipe();
        recipe1.setName("Recipe 1");
        Recipe recipe2 = new Recipe();
        recipe2.setName("Recipe 2");
        when(recipeRepository.findAll()).thenReturn(Arrays.asList(recipe1, recipe2));

        List<RecipeDTO> result = recipeService.getAllRecipes();

        assertEquals(2, result.size());
        assertEquals("Recipe 1", result.get(0).getName());
        assertEquals("Recipe 2", result.get(1).getName());
    }

    @Test
    public void testFilterRecipes() {
        // Arrange
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Recipe.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Recipe.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        Recipe recipe = new Recipe();
        recipe.setName("Test Recipe");
        when(typedQuery.getResultList()).thenReturn(List.of(recipe));

        List<Recipe> result = recipeService.filterRecipes(null, null, null, null, null);

        assertEquals(1, result.size());
        assertEquals("Test Recipe", result.get(0).getName());
    }

    @Test
    public void testFilterRecipesWithParameterVegetarian() {
        // Arrange
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Recipe.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Recipe.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        Recipe recipe = new Recipe();
        recipe.setName("Test Recipe");
        recipe.setVegetarian(true);
        when(typedQuery.getResultList()).thenReturn(List.of(recipe));

        List<Recipe> result = recipeService.filterRecipes(true, null, null, null, null);

        assertEquals(1, result.size());
        assertTrue(result.get(0).isVegetarian());
    }

    @Test
    public void testDeleteRecipe() {
        Long recipeId = 1L;
        Recipe recipe = new Recipe();
        recipe.setName("Test Recipe");
        recipe.setInstructions("Test Instructions");
        recipe.setPreparationTime(30);
        recipe.setServings(4);
        recipe.setDifficulty(Difficulty.MEDIUM);
        recipe.setVegetarian(false);

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        recipeService.deleteRecipe(recipeId);

        verify(recipeRepository, times(1)).delete(recipe);
    }


}

