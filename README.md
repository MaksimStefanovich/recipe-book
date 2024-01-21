# Recipe Book

## Description
Recipe Book is a Java application that provides a public API for managing recipes. It uses Spring Boot, Hibernate, 
Docker for running a PostgreSQL database and utilizes Swagger for API documentation.
The application supports operations such as adding, updating, retrieving, filtering, and deleting recipes.

## Getting Started

### Prerequisites
- Java 17
- Maven
- Docker


### Installation
1. Clone the repository
```bash
'git clone https://github.com/MaksimStefanovich/recipe-book'
```
2. Navigate to the project directory
```bash
'cd recipe-book'
```
3. Build the project with Maven
```bash
'mvn clean install'
```
4. Run the PostgreSQL Docker container
```bash
' docker-compose up'
```


### Usage
The application provides the following endpoints:

* POST /api/recipes: Add a new recipe. Takes a RecipeDTO object as input and returns the added recipe as a RecipeDTO object.
* PUT /api/recipes/{recipeId}: Update an existing recipe. Takes the ID of the recipe to be updated and a RecipeDTO object containing the updated details. Returns the updated recipe as a RecipeDTO object.
* GET /api/recipes: Get all recipes. Returns all recipes from the database as a list of RecipeDTO objects.
* GET /api/recipes/filter: Filter recipes based on criteria such as whether they are vegetarian, the number of servings they have, the ingredients they include or exclude, and a search text. Returns a list of recipes that match the given criteria.
* DELETE /api/recipes/{recipeId}: Delete a recipe by ID. Takes the ID of the recipe to be deleted. Does not return any content.

### Project Configuration
#### Maven Configuration
This project uses Maven for dependency management. Ensure that you have Maven installed and configured.

#### Spring Boot Version
The project is based on Spring Boot version 3.2.1.

#### Dependencies
- spring-boot-starter-data-jpa
- spring-boot-starter-web
- spring-boot-starter-validation
- spring-boot-starter-hateoas
- spring-boot-starter-actuator
- hibernate-core version 6.3.1.Final
- spring-boot-devtools

### Built With
- Java 17
- Spring Boot
- Hibernate
- Docker
- PostgresSQL
- Swagger

### Swagger Documentation

The Swagger UI for this application can be accessed at http://localhost:8080/swagger-ui.html. 
This page provides an interactive documentation of the available APIs.
(replace localhost:8080 with your server’s address if you’re not running the application locally).

## Testing
This project includes both unit and integration tests. To run these tests, navigate to the project directory and run the following command:
```bash
mvn test
```

### Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
