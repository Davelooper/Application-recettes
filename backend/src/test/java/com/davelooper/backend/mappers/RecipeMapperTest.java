package com.davelooper.backend.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import com.davelooper.backend.dtos.RecipeCreateRequestDTO;
import com.davelooper.backend.dtos.RecipeFullResponseDTO;
import com.davelooper.backend.dtos.RecipeSummaryResponseDTO;
import com.davelooper.backend.dtos.RecipeUpdateRequestDTO;
import com.davelooper.backend.entities.Recipe;
import com.davelooper.backend.entities.User;

@DisplayName("Unit Tests - RecipeMapper")
class RecipeMapperTest {

  private final RecipeMapper mapper = Mappers.getMapper(RecipeMapper.class);
  private final RecipeIngredientMapper recipeIngredientMapper =
      Mappers.getMapper(RecipeIngredientMapper.class);
  private final RecipeStepMapper recipeStepMapper = Mappers.getMapper(RecipeStepMapper.class);
  private final IngredientMapper ingredientMapper = Mappers.getMapper(IngredientMapper.class);
  private final SeasonalityMapper seasonalityMapper = Mappers.getMapper(SeasonalityMapper.class);

  public RecipeMapperTest() {
    // Injection de la hiérarchie complète
    ReflectionTestUtils.setField(ingredientMapper, "seasonalityMapper", seasonalityMapper);
    ReflectionTestUtils.setField(recipeIngredientMapper, "ingredientMapper", ingredientMapper);
    ReflectionTestUtils.setField(mapper, "recipeIngredientMapper", recipeIngredientMapper);
    ReflectionTestUtils.setField(mapper, "recipeStepMapper", recipeStepMapper);
  }

  @Test
  @DisplayName("Doit calculer le temps total correctement")
  void shouldCalculateTotalTime() {
    // GIVEN
    Recipe entity = Recipe.builder().prepTimeMinutes(15).cookTimeMinutes(30).build();

    // WHEN
    RecipeSummaryResponseDTO response = mapper.toSummaryResponseDTO(entity);

    // THEN
    assertThat(response.totalTimeMinutes()).isEqualTo(45);
  }

  @Test
  @DisplayName("Doit transformer en Réponse complète avec auteur ID")
  void shouldMapToFullResponse() {
    // GIVEN
    User author = User.builder().id(99L).username("chef").build();
    Recipe entity =
        Recipe.builder().id(1L).title("Tarte").user(author).recipeIngredients(new ArrayList<>())
            .recipeSteps(new ArrayList<>()).createdAt(LocalDateTime.now()).build();

    // WHEN
    RecipeFullResponseDTO response = mapper.toFullResponseDTO(entity);

    // THEN
    assertThat(response.authorId()).isEqualTo(99L);
    assertThat(response.title()).isEqualTo("Tarte");
  }

  @Test
  @DisplayName("Doit transformer une Création en Entité (en ignorant les champs sensibles)")
  void shouldMapCreateToEntity() {
    // GIVEN
    RecipeCreateRequestDTO request =
      new RecipeCreateRequestDTO("Pizza", "Belle pizza", 4, 2, 20, 15, 10L, List.of(), List.of());

    // WHEN
    Recipe entity = mapper.toEntity(request);

    // THEN
    assertThat(entity.getTitle()).isEqualTo("Pizza");
    assertThat(entity.getId()).isNull(); // Sécurité
    assertThat(entity.getUser()).isNull(); // Doit être résolu dans le service via authorId
  }

  @Test
  @DisplayName("Doit mettre à jour une Entité existante (stratégie IGNORE null)")
  void shouldUpdateEntityIgnoringNulls() {
    // GIVEN
    Recipe existing = Recipe.builder().id(1L).title("Ancien Titre").difficulty(5).build();

    // On ne veut changer que le titre
    RecipeUpdateRequestDTO request =
        new RecipeUpdateRequestDTO("Nouveau Titre", null, null, null, null, null, null, null);

    // WHEN
    mapper.updateEntityFromRequest(request, existing);

    // THEN
    assertThat(existing.getTitle()).isEqualTo("Nouveau Titre");
    assertThat(existing.getDifficulty()).isEqualTo(5); // Conservé car null dans le DTO
  }
}
