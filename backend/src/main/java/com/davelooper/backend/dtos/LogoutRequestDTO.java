package com.davelooper.backend.dtos;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequestDTO(
    @NotBlank(message = "Le token de rafraichissement est requis") String refreshToken) {}
