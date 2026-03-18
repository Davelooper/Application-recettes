package com.davelooper.backend.dtos;

public record LoginResponseDTO(
    String token,
    Long id,
    String username,
    String email,
    String role) {
}
