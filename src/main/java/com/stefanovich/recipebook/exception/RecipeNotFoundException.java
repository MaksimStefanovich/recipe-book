package com.stefanovich.recipebook.exception;

import jakarta.persistence.EntityNotFoundException;

public class RecipeNotFoundException extends EntityNotFoundException {
    public RecipeNotFoundException(String message) {
        super(message);
    }
}