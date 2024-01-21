--changeset stefanovich:init_schema
--comment: init data base

-- Create Recipe Table
CREATE TABLE Recipe (
                        id SERIAL PRIMARY KEY,
                        name TEXT NOT NULL,
                        instructions TEXT,
                        preparation_time INTEGER,
                        servings INTEGER,
                        difficulty VARCHAR(63),
                        vegetarian BOOLEAN NOT NULL
);

-- Create Ingredient Table
CREATE TABLE Ingredient (
                            id SERIAL PRIMARY KEY,
                            name TEXT NOT NULL
);

-- Create IngredientInRecipe Table
CREATE TABLE IngredientInRecipe (
                                    id SERIAL PRIMARY KEY,
                                    ingredient_id INTEGER,
                                    recipe_id INTEGER,
                                    quantity DOUBLE PRECISION,
                                    unit_of_measure TEXT NOT NULL,
                                    FOREIGN KEY (ingredient_id) REFERENCES Ingredient(id),
                                    FOREIGN KEY (recipe_id) REFERENCES Recipe(id)
);

-- Insert Default Recipe
INSERT INTO Recipe (name, instructions, preparation_time, difficulty, vegetarian)
VALUES ('Default Recipe', 'This is a sample recipe', 30, 'MEDIUM', FALSE);
INSERT INTO Recipe (name, instructions, preparation_time, difficulty, vegetarian)
VALUES ('Test', 'This is a salmon in oven', 25, 'HARD', TRUE);

-- Insert Ingredients for Default Recipe
INSERT INTO Ingredient (name) VALUES ('Ingredient1');
INSERT INTO Ingredient (name) VALUES ('Ingredient2');
INSERT INTO Ingredient (name) VALUES ('potatoes');
INSERT INTO Ingredient (name) VALUES ('salmon');

-- Get IDs of inserted ingredients
DO
$$
    DECLARE
        ingredient1_id INTEGER;
        ingredient2_id INTEGER;
        potatoes_id INTEGER;
        salmon_id INTEGER;
    BEGIN
        SELECT id INTO ingredient1_id FROM Ingredient WHERE name = 'Ingredient1';
        SELECT id INTO ingredient2_id FROM Ingredient WHERE name = 'Ingredient2';
        SELECT id INTO potatoes_id FROM Ingredient WHERE name = 'potatoes';
        SELECT id INTO salmon_id FROM Ingredient WHERE name = 'salmon';

        -- Insert IngredientsInRecipe for Default Recipe
        INSERT INTO IngredientInRecipe (ingredient_id, recipe_id, quantity, unit_of_measure)
        VALUES (ingredient1_id, 1, 100, 'g');
        INSERT INTO IngredientInRecipe (ingredient_id, recipe_id, quantity, unit_of_measure)
        VALUES (ingredient2_id, 1, 50, 'g');
        INSERT INTO IngredientInRecipe (ingredient_id, recipe_id, quantity, unit_of_measure)
        VALUES (salmon_id, 2, 75, 'g');
        INSERT INTO IngredientInRecipe (ingredient_id, recipe_id, quantity, unit_of_measure)
        VALUES (potatoes_id, 2, 150, 'g');
    END
$$;
