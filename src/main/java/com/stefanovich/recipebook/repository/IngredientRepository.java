package com.stefanovich.recipebook.repository;

import com.stefanovich.recipebook.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository  extends JpaRepository<Ingredient, Integer> {
    Optional<Ingredient> findByName(String name);
}
