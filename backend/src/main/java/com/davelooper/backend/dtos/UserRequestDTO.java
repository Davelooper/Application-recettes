package com.davelooper.backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.davelooper.backend.validation.PasswordMatches;

/**
 * Ce que l'on reçoit du client (ex: formulaire d'inscription)
 */
@PasswordMatches
public record UserRequestDTO(
    @NotBlank(message = "L'email est requis")
    @Email(message = "Format d'email invalide")
    String email,

    @Size(max = 100)
    String username,

    @NotBlank(message = "Le mot de passe est requis")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    String password, // Note : "password" en clair, pas "passwordHash"

    @NotBlank(message = "La confirmation du mot de passe est requise")
    String passwordConfirm
) {}