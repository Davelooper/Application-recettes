package com.davelooper.backend.dtos;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO pour la mise à jour d'une recette existante.
 * Les champs sont optionnels pour permettre des mises à jour partielles (PATCH style).
 */
public record RecipeUpdateRequestDTO(
    String title,
    String description,
    Integer servings,
    Integer difficulty,
    Integer prepTimeMinutes,
    Integer cookTimeMinutes,
    List<RecipeIngredientUpdateRequestDTO> ingredients,
    List<RecipeStepUpdateRequestDTO> steps
) {
    /**
     * DTO interne pour la mise à jour d'ingrédients.
     */
    public record RecipeIngredientUpdateRequestDTO(
        Long ingredientId,
        BigDecimal quantity,
        Long unitId
    ) {}

    /**
     * DTO interne pour la mise à jour d'étapes.
     */
    public record RecipeStepUpdateRequestDTO(
        Integer stepNumber,
        String description
    ) {}
}
