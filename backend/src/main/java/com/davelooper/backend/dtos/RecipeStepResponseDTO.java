package com.davelooper.backend.dtos;

/**
 * DTO pour une étape de préparation d'une recette. Évite la dépendance circulaire vers Recipe.
 */
public record RecipeStepResponseDTO(Integer stepNumber, String description) {
}
