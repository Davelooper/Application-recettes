package com.davelooper.backend.dtos;

import java.time.LocalDateTime;

/**
 * Ce que l'on renvoie au client (Données sécurisées)
 */
public record UserResponseDTO(
    Long id,
    String email,
    String username,
    String role,
    LocalDateTime createdAt
) {}