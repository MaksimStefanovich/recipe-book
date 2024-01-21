package com.stefanovich.recipebook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stefanovich.recipebook.model.dto.RecipeDTO;
import com.stefanovich.recipebook.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class RecipeApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RecipeService recipeService;

    @Test
    public void testAddRecipe() throws Exception {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setName("Test Recipe");
        recipeDTO.setInstructions("Test Instructions");
        recipeDTO.setPreparationTime(30);
        recipeDTO.setServings(4);
        recipeDTO.setDifficulty("MEDIUM");
        recipeDTO.setVegetarian(false);

        mockMvc.perform(post("/api/v1/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeDTO)))
                .andExpect(status().isCreated());
    }
    @Test
    public void testUpdateRecipe() throws Exception {
        long recipeId = 1L; // replace with a valid ID
        RecipeDTO updatedRecipeDTO = new RecipeDTO();
        updatedRecipeDTO.setName("Updated Recipe");
        updatedRecipeDTO.setInstructions("Updated Instructions");
        updatedRecipeDTO.setPreparationTime(35);
        updatedRecipeDTO.setServings(5);
        updatedRecipeDTO.setDifficulty("HARD");
        updatedRecipeDTO.setVegetarian(true);
        updatedRecipeDTO.setIngredients(new ArrayList<>());

        mockMvc.perform(put("/api/v1/recipes/" + recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRecipeDTO)))
                .andExpect(status().isOk());
    }


    @Test
    public void testGetAllRecipes() throws Exception {
        mockMvc.perform(get("/api/v1/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    public void testFilterRecipes() throws Exception {
        mockMvc.perform(get("/api/v1/recipes/filter")
                        .param("vegetarian", "true")
                        .param("servings", "4")
                        .param("includeIngredients", "tomato", "lettuce")
                        .param("excludeIngredients", "beef")
                        .param("searchText", "salad")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    public void testDeleteRecipe() throws Exception {
        Long recipeId = 1L;

        mockMvc.perform(delete("/api/v1/recipes/" + recipeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(recipeService, Mockito.times(1)).deleteRecipe(recipeId);
    }
}
