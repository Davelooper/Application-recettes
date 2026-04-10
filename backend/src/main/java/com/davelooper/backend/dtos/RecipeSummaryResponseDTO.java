package com.davelooper.backend.dtos;

/** DTO résumé pour l'affichage des cartes de recettes dans le front-end. */
public record RecipeSummaryResponseDTO(
    Long id,
    String title,
    Integer difficulty,
    Integer totalTimeMinutes,
    Integer servings,
    String imageUrl) {}
