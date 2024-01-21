package com.stefanovich.recipebook.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientDTO {
    Integer id;
    @Size(min = 2, message = "name should have at least 2 characters")
    @NotBlank
    String name;
}
