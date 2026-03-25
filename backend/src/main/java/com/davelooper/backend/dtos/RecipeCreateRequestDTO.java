package com.davelooper.backend.dtos;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO pour la création d'une nouvelle recette.
 */
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RecipeCreateRequestDTO(
    String title, 
    String description, 
    Integer servings,
    Integer difficulty, 
    Integer prepTimeMinutes, 
    Integer cookTimeMinutes, 
    Long authorId,
    @NotNull(message = "La liste des ingrédients est obligatoire.")
    @NotEmpty(message = "La liste des ingrédients ne peut pas être vide.")
    List<RecipeIngredientRequestDTO> ingredients, 
    @NotNull(message = "La liste des étapes est obligatoire.")
    @NotEmpty(message = "La liste des étapes ne peut pas être vide.")
    List<RecipeStepRequestDTO> steps) {
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
