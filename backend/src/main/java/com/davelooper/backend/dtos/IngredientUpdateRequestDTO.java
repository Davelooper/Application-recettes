package com.davelooper.backend.dtos;

import jakarta.validation.constraints.Size;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

/**
 * Donnees recues depuis le front pour modifier un ingredient. imageUrl ne vient jamais du client:
 * elle est mise a jour par le serveur apres upload.
 */
public record IngredientUpdateRequestDTO(
    @Size(
            min = 1,
            max = 100,
            message = "Le nom de l'ingredient doit contenir entre 1 et 100 caracteres")
        String name,
    Set<Long> seasonalityIds,
    MultipartFile image) {}
