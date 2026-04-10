package com.davelooper.backend.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import com.davelooper.backend.dtos.RecipeStepResponseDTO;
import com.davelooper.backend.entities.RecipeStep;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

@DisplayName("Unit Tests - RecipeStepMapper")
class RecipeStepMapperTest {

  private final RecipeStepMapper mapper = Mappers.getMapper(RecipeStepMapper.class);

  @Test
  @DisplayName("Doit transformer une Entité RecipeStep en RecipeStepResponseDTO")
  void shouldMapEntityToResponse() {
    // GIVEN
    RecipeStep entity =
        RecipeStep.builder().id(1L).stepNumber(1).description("Éplucher les légumes").build();

    // WHEN
    RecipeStepResponseDTO response = mapper.toResponse(entity);

    // THEN
    assertThat(response).isNotNull();
    assertThat(response.stepNumber()).isEqualTo(entity.getStepNumber());
    assertThat(response.description()).isEqualTo(entity.getDescription());
  }

  @Test
  @DisplayName("Doit retourner null si l'entité est null")
  void shouldReturnNullWhenEntityIsNull() {
    // WHEN
    RecipeStepResponseDTO response = mapper.toResponse(null);

    // THEN
    assertThat(response).isNull();
  }
}
