package com.stefanovich.recipebook.model;

import lombok.Getter;

@Getter
public enum UnitOfMeasure {
    KILOGRAM("kg"),
    POUND("lb"),
    GRAM("g"),
    OUNCE("oz");

    private final String unit;

    UnitOfMeasure(String unit) {
        this.unit = unit;
    }

}
