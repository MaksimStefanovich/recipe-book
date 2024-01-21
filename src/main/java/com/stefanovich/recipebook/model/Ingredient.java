package com.stefanovich.recipebook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @NotBlank
    @Size(min = 2, max = 100)
    String name;

    @Override
    public String toString() {
        return "Ingredient [id=" + id + ", name=" + name + "]";  // do not include recipes
    }

}
