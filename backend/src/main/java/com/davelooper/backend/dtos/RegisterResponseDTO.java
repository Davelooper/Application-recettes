package com.davelooper.backend.dtos;

import java.time.LocalDateTime;

/**
 * Ce que l'on renvoie au client (Données sécurisées)
 */
public record RegisterResponseDTO(
    Long id,
    String email,
    String username,
    String role,
    LocalDateTime createdAt
) {}