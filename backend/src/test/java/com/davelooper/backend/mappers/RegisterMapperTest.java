package com.davelooper.backend.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.davelooper.backend.dtos.RegisterRequestDTO;
import com.davelooper.backend.dtos.RegisterResponseDTO;
import com.davelooper.backend.entities.User;

@DisplayName("Unit Tests - RegisterMapper")
class RegisterMapperTest {

  // On récupère l'instance générée par MapStruct
  private final RegisterMapper mapper = Mappers.getMapper(RegisterMapper.class);

  @Test
  @DisplayName("Doit transformer une Requête en Entité (Entrée)")
  void shouldMapRequestToEntity() {
    // GIVEN
    RegisterRequestDTO request = new RegisterRequestDTO(
        "chef@cuisine.fr", 
        "Ratatouille", 
        "password123",
        "password123"
    );

    // WHEN
    User entity = mapper.toEntity(request);

    // THEN
    assertThat(entity.getEmail()).isEqualTo(request.email());
    assertThat(entity.getUsername()).isEqualTo(request.username());
    
    // VERIFICATION DES SECURITES
    assertThat(entity.getPasswordHash()).isNull(); // Le mapper doit ignorer le mot de passe en clair
    assertThat(entity.getId()).isNull();           // L'ID ne doit pas être mappé depuis la requête
    assertThat(entity.getRole()).isEqualTo("standard"); // Le rôle a une valeur par défaut dans l'entité
  }

  @Test
  @DisplayName("Doit transformer une Entité en Réponse (Sortie)")
  void shouldMapEntityToResponse() {
    // GIVEN
    LocalDateTime now = LocalDateTime.now();
    User entity = User.builder()
        .id(1L)
        .email("test@davelooper.com")
        .username("DaveLooper")
        .passwordHash("$2a$12$hashedpassword") // Donnée sensible
        .role("admin")
        .createdAt(now)
        .build();

  

    // WHEN
    RegisterResponseDTO response = mapper.toResponse(entity);

    // THEN
    assertThat(response.id()).isEqualTo(entity.getId());
    assertThat(response.email()).isEqualTo(entity.getEmail());
    assertThat(response.username()).isEqualTo(entity.getUsername());
    assertThat(response.role()).isEqualTo(entity.getRole());
    assertThat(response.createdAt()).isEqualTo(entity.getCreatedAt());
    
    // La preuve ultime : le record n'a même pas de champ pour le mot de passe
    // (Le test ne compilerait pas si on essayait d'y accéder)
  }
}