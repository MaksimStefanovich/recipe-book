package com.stefanovich.recipebook.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ingredientinrecipe")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientInRecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @NotNull(message = "ingredient should not be null")
    @ManyToOne
    Ingredient ingredient;
    @NotNull(message = "recipe should not be null")
    @ManyToOne
    @JsonBackReference
    Recipe recipe;
    @Min(value = 0, message = "quantity should be at least 0")
    double quantity;
    String unitOfMeasure = UnitOfMeasure.GRAM.getUnit();

    @Override
    public String toString() {
        return "Ingredient [id=" + id + ", name=" + ingredient.getName() + ", quantity=" + quantity + "]";  // do not include recipes
    }

}
