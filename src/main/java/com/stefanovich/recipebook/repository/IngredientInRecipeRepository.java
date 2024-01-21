package com.stefanovich.recipebook.repository;

import com.stefanovich.recipebook.model.IngredientInRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientInRecipeRepository extends JpaRepository<IngredientInRecipe, Integer> {
}
