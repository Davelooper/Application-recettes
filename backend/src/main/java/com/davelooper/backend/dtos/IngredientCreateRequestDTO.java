package com.davelooper.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

/**
 * Donnees recues depuis le front pour creer un ingredient. L'image est envoyee en fichier; imageUrl
 * est calculee cote serveur apres stockage.
 */
public record IngredientCreateRequestDTO(
    @NotBlank(message = "Le nom de l'ingredient est requis")
        @Size(max = 100, message = "Le nom de l'ingredient ne doit pas depasser 100 caracteres")
        String name,
    Set<Long> seasonalityIds,
    MultipartFile image) {}
