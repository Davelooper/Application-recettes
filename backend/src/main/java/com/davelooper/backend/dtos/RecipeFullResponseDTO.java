package com.davelooper.backend.dtos;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO complet pour l'affichage détaillé d'une recette. Inclut les ingrédients, les étapes et les
 * informations de l'auteur.
 */
public record RecipeFullResponseDTO(Long id, String title, String description, Integer servings,
    Integer difficulty, String imageUrl, Integer prepTimeMinutes, Integer cookTimeMinutes,
    Integer totalTimeMinutes, LocalDateTime createdAt, Long authorId,
    List<RecipeIngredientResponseDTO> ingredients, List<RecipeStepResponseDTO> steps) {
}
