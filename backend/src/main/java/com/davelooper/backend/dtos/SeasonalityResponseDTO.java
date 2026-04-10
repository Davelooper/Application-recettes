package com.davelooper.backend.dtos;

/** Version legere de la saisonnalite exposee dans les reponses ingredient. */
public record SeasonalityResponseDTO(
    Long id, String periodName, Integer startMonth, Integer endMonth) {}
