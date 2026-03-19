package com.davelooper.backend.dtos;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO pour la création d'une nouvelle recette.
 */
public record RecipeCreateRequestDTO(String title, String description, Integer servings,
    Integer difficulty, Integer prepTimeMinutes, Integer cookTimeMinutes, Long authorId,
    List<RecipeIngredientRequestDTO> ingredients, List<RecipeStepRequestDTO> steps) {
  /**
   * DTO interne pour l'ajout d'ingrédients lors de la création.
   */
  public record RecipeIngredientRequestDTO(Long ingredientId, BigDecimal quantity, Long unitId) {
  }

  /**
   * DTO interne pour l'ajout d'étapes lors de la création.
   */
  public record RecipeStepRequestDTO(Integer stepNumber, String description) {
  }
}
