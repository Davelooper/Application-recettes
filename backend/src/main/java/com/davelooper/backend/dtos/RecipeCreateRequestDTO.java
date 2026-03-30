package com.davelooper.backend.dtos;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RecipeCreateRequestDTO(
    @NotBlank(message = "Le titre est obligatoire.")
    String title,

    String description,

    @NotNull(message = "Le nombre de portions est obligatoire.")
    @Min(value = 1, message = "Le nombre de portions doit etre superieur a 0.")
    Integer servings,

    @NotNull(message = "La difficulte est obligatoire.")
    Integer difficulty,

    @NotNull(message = "Le temps de preparation est obligatoire.")
    Integer prepTimeMinutes,

    @NotNull(message = "Le temps de cuisson est obligatoire.")
    Integer cookTimeMinutes,

    @NotNull(message = "L'auteur est obligatoire.")
    Long authorId,

    @NotNull(message = "La liste des ingredients est obligatoire.")
    @NotEmpty(message = "La liste des ingredients ne peut pas etre vide.")
    List<@Valid RecipeIngredientRequestDTO> ingredients,

    @NotNull(message = "La liste des etapes est obligatoire.")
    @NotEmpty(message = "La liste des etapes ne peut pas etre vide.")
    List<@Valid RecipeStepRequestDTO> steps
) {
    public record RecipeIngredientRequestDTO(
        @NotNull(message = "L'identifiant de l'ingredient est obligatoire.")
        Long ingredientId,

        @NotNull(message = "La quantite est obligatoire.")
        @DecimalMin(value = "0.0", inclusive = false, message = "La quantite doit etre superieure a 0.")
        BigDecimal quantity,

        @NotNull(message = "L'identifiant de l'unite est obligatoire.")
        Long unitId
    ) {}

    public record RecipeStepRequestDTO(
        @NotNull(message = "Le numero d'etape est obligatoire.")
        Integer stepNumber,

        @NotBlank(message = "La description de l'etape est obligatoire.")
        String description
    ) {}
}
