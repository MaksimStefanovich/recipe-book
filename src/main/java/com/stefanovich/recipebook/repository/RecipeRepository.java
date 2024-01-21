package com.stefanovich.recipebook.repository;

import com.stefanovich.recipebook.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}



