package com.davelooper.backend.dtos;

import java.math.BigDecimal;

/**
 * DTO pour l'ingrédient spécifique à une recette (quantité + unité). Évite la dépendance circulaire
 * vers Recipe.
 */
public record RecipeIngredientResponseDTO(Long id, IngredientResponseDTO ingredient,
    BigDecimal quantity, String unitName) {
}
