package com.davelooper.backend.dtos;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDTO(
    @NotBlank(message = "Le token de rafraîchissement est requis") String refreshToken) {

}
