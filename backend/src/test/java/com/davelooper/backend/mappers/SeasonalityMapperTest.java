package com.davelooper.backend.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import com.davelooper.backend.dtos.SeasonalityResponseDTO;
import com.davelooper.backend.entities.Seasonality;

@DisplayName("Unit Tests - SeasonalityMapper")
class SeasonalityMapperTest {

  private final SeasonalityMapper mapper = Mappers.getMapper(SeasonalityMapper.class);

  @Test
  @DisplayName("Doit transformer une Entité Seasonality en SeasonalityResponseDTO")
  void shouldMapEntityToResponse() {
    // GIVEN
    Seasonality entity =
        Seasonality.builder().id(1L).periodName("Hiver").startMonth(12).endMonth(2).build();

    // WHEN
    SeasonalityResponseDTO response = mapper.toResponse(entity);

    // THEN
    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo(entity.getId());
    assertThat(response.periodName()).isEqualTo(entity.getPeriodName());
    assertThat(response.startMonth()).isEqualTo(entity.getStartMonth());
    assertThat(response.endMonth()).isEqualTo(entity.getEndMonth());
  }

  @Test
  @DisplayName("Doit retourner null si l'entité est null")
  void shouldReturnNullWhenEntityIsNull() {
    // WHEN
    SeasonalityResponseDTO response = mapper.toResponse(null);

    // THEN
    assertThat(response).isNull();
  }
}
