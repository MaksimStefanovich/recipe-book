package com.stefanovich.recipebook.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientInRecipeDTO {
    Integer id;
    @NotNull(message = "ingredient should not be null")
    IngredientDTO ingredient;
    @NotNull(message = "recipeId should not be null")
    Integer recipeId;
    @Min(value = 0, message = "quantity should be at least 0")
    double quantity;
    @NotBlank(message = "unitOfMeasure should not be blank")
    String unitOfMeasure;
}
