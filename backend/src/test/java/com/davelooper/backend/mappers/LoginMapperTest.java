package com.davelooper.backend.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.davelooper.backend.dtos.LoginResponseDTO;
import com.davelooper.backend.entities.User;

@DisplayName("Unit Tests - LoginMapper")
public class LoginMapperTest {

  private final LoginMapper mapper = Mappers.getMapper(LoginMapper.class);

  @Test
  @DisplayName("Doit transformer une Entité en Réponse (Sortie)")
  void shouldMapEntityToResponse() {
    LocalDateTime now = LocalDateTime.now();
    User entity = User.builder()
        .id(1L)
        .email("test@davelooper.com")
        .username("DaveLooper")
        .passwordHash("$2a$12$hashedpassword") // Donnée sensible
        .role("admin")
        .createdAt(now)
        .build();

    LoginResponseDTO response = mapper.toResponse(entity, "test");

    assertThat(response.id()).isEqualTo(entity.getId());
    assertThat(response.email()).isEqualTo(entity.getEmail());
    assertThat(response.username()).isEqualTo(entity.getUsername());
    assertThat(response.role()).isEqualTo(entity.getRole());
    assertThat(response.token()).isEqualTo("test");

  }

}
