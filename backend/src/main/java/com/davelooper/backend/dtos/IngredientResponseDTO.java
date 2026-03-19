package com.davelooper.backend.dtos;

import java.util.Set;

/**
 * Donnees retournees au front pour un ingredient.
 * imageUrl contient le chemin/URL final construit cote serveur.
 */
public record IngredientResponseDTO(
    Long id,
    String name,
    String imageUrl,
    Set<SeasonalityResponseDTO> seasonalities
) {
}
