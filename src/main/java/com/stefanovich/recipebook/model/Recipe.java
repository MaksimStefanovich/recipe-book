package com.stefanovich.recipebook.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @NotBlank
    @Size(min = 2, max = 100)
    String name;
    @NotBlank
    String instructions;
    @NotNull
    @Min(1)
    Integer preparationTime;
    @NotNull
    @Min(1)
    Integer servings;
    @NotNull
    @Enumerated(EnumType.STRING)
    Difficulty difficulty = Difficulty.MEDIUM;
    boolean vegetarian = false;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @JsonManagedReference
    List<IngredientInRecipe> ingredients = new ArrayList<>();

    public Recipe() {

    }
}
