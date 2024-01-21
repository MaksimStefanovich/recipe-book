package com.stefanovich.recipebook.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipeDTO {
    Integer id;
    @Size(min = 2, message = "name should have at least 2 characters")
    @NotBlank
    String name;
    @NotEmpty(message = "instructions should not be empty")
    String instructions;
    @Min(value = 1, message = "preparationTime should be at least 1")
    Integer preparationTime;
    @Min(value = 1, message = "servings should be at least 1")
    Integer servings;
    @NotNull(message = "difficulty should not be null")
    String difficulty;
    boolean vegetarian;
    List<IngredientInRecipeDTO> ingredients = new ArrayList<>();
}
