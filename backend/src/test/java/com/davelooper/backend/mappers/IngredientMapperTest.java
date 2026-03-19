package com.davelooper.backend.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import com.davelooper.backend.dtos.IngredientCreateRequestDTO;
import com.davelooper.backend.dtos.IngredientResponseDTO;
import com.davelooper.backend.dtos.SeasonalityResponseDTO;
import com.davelooper.backend.entities.Ingredient;
import com.davelooper.backend.entities.Seasonality;

@DisplayName("Unit Tests - IngredientMapper")
class IngredientMapperTest {

  private final IngredientMapper mapper = Mappers.getMapper(IngredientMapper.class);
  private final SeasonalityMapper seasonalityMapper = Mappers.getMapper(SeasonalityMapper.class);

  public IngredientMapperTest() {
    // Injection manuelle du mapper délégué (nécessaire car on n'utilise pas le contexte Spring)
    ReflectionTestUtils.setField(mapper, "seasonalityMapper", seasonalityMapper);
  }

  @Test
  @DisplayName("Doit transformer une Requête de création en Entité")
  void shouldMapCreateRequestToEntity() {
    // GIVEN
    MultipartFile mockFile = mock(MultipartFile.class);
    IngredientCreateRequestDTO request =
        new IngredientCreateRequestDTO("Tomate", Set.of(1L, 2L), mockFile);

    // WHEN
    Ingredient entity = mapper.toEntity(request);

    // THEN
    assertThat(entity.getName()).isEqualTo(request.name());

    // SECURITE : L'ID ne doit pas être mappé depuis la requête
    assertThat(entity.getId()).isNull();

    // SECURITE : L'image n'est pas mappée automatiquement (gérée par le service)
    assertThat(entity.getImageUrl()).isNull();
  }

  @Test
  @DisplayName("Doit transformer une Entité en Réponse (avec seasonalités)")
  void shouldMapEntityToResponse() {
    // GIVEN
    Seasonality s1 =
        Seasonality.builder().id(1L).periodName("Été").startMonth(6).endMonth(8).build();
    Seasonality s2 =
        Seasonality.builder().id(2L).periodName("Automne").startMonth(9).endMonth(11).build();

    Ingredient entity = Ingredient.builder().id(10L).name("Courge").imageUrl("uploads/courge.png")
        .seasonalities(new HashSet<>(List.of(s1, s2))).build();

    // WHEN
    IngredientResponseDTO response = mapper.toResponse(entity);

    // THEN
    assertThat(response.id()).isEqualTo(entity.getId());
    assertThat(response.name()).isEqualTo(entity.getName());
    assertThat(response.imageUrl()).isEqualTo(entity.getImageUrl());

    // Vérification de la délégation au SeasonalityMapper
    assertThat(response.seasonalities()).hasSize(2);
    assertThat(response.seasonalities()).extracting(SeasonalityResponseDTO::periodName)
        .containsExactlyInAnyOrder("Été", "Automne");
  }

  @Test
  @DisplayName("Doit retourner null si l'entrée est null")
  void shouldReturnNullWhenInputIsNull() {
    assertThat(mapper.toEntity(null)).isNull();
    assertThat(mapper.toResponse(null)).isNull();
  }
}
