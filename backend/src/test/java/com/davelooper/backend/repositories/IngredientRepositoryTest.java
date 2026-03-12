package com.davelooper.backend.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import com.davelooper.backend.entities.Ingredient;
import com.davelooper.backend.entities.Seasonality;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Tests du Repository Ingredient")
class IngredientRepositoryTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

  @Autowired
  private IngredientRepository ingredientRepository;

  @Autowired
  private SeasonalityRepository seasonalityRepository;

  @Nested
  @DisplayName("Tests de persistance de base")
  class PersistenceTests {

    @Test
    @DisplayName("Doit enregistrer un ingrédient avec tous ses champs")
    void shouldSaveFullIngredient() {
      Ingredient ingredient = Ingredient.builder().name("Avocat")
          .imageUrl("https://cdn.example.com/avocado.jpg").build();

      Ingredient saved = ingredientRepository.save(ingredient);

      assertThat(saved.getId()).isNotNull();
      assertThat(saved.getName()).isEqualTo("Avocat");
      assertThat(saved.getImageUrl()).isEqualTo("https://cdn.example.com/avocado.jpg");
    }

    @Test
    @DisplayName("Doit échouer si le nom est null (Contrainte NOT NULL)")
    void shouldFailWhenNameIsNull() {
      Ingredient invalid = Ingredient.builder().imageUrl("http://image.com").build();

      // On utilise saveAndFlush pour forcer l'exécution de l'INSERT SQL
      assertThatThrownBy(() -> ingredientRepository.saveAndFlush(invalid))
          .isInstanceOf(DataIntegrityViolationException.class);
    }
  }

  @Nested
  @DisplayName("Tests des relations ManyToMany")
  class RelationshipTests {

    @Test
    @DisplayName("Doit persister et récupérer la liaison avec plusieurs saisons")
    void shouldHandleMultipleSeasons() {
      // Given: On prépare les saisons
      Seasonality spring = seasonalityRepository
          .save(Seasonality.builder().periodName("Printemps").startMonth(3).endMonth(5).build());
      Seasonality summer = seasonalityRepository
          .save(Seasonality.builder().periodName("Été").startMonth(6).endMonth(8).build());

      Ingredient asparagus =
          Ingredient.builder().name("Asperge").seasonalities(Set.of(spring, summer)).build();

      // When
      Ingredient saved = ingredientRepository.save(asparagus);
      ingredientRepository.flush(); // Force la synchro

      // Then
      Ingredient fetched = ingredientRepository.findById(saved.getId()).orElseThrow();
      assertThat(fetched.getSeasonalities()).hasSize(2).extracting(Seasonality::getPeriodName)
          .containsExactlyInAnyOrder("Printemps", "Été");
    }

    @Test
    @DisplayName("Doit permettre de supprimer une saison de l'ingrédient sans supprimer la saison elle-même")
    void shouldRemoveSeasonLinkButKeepSeasonEntity() {
      // Given
      Seasonality winter = seasonalityRepository
          .save(Seasonality.builder().periodName("Hiver").startMonth(12).endMonth(2).build());
      Ingredient leek = ingredientRepository
          .save(Ingredient.builder().name("Poireau").seasonalities(Set.of(winter)).build());

      // When: On vide les saisons de l'ingrédient
      leek.getSeasonalities().clear();
      ingredientRepository.saveAndFlush(leek);

      // Then
      assertThat(ingredientRepository.findById(leek.getId()).get().getSeasonalities()).isEmpty();
      assertThat(seasonalityRepository.existsById(winter.getId())).isTrue(); // La saison existe
                                                                             // toujours
    }
  }

  @Nested
  @DisplayName("Tests des comportements Lombok/Builder")
  class LogicTests {

    @Test
    @DisplayName("Le HashSet doit être initialisé par défaut même avec le Builder")
    void shouldHaveInitializedSetByDefault() {
      // On vérifie que @Builder.Default fonctionne
      Ingredient ingredient = Ingredient.builder().name("Sel").build();

      assertThat(ingredient.getSeasonalities()).isNotNull();
      assertThat(ingredient.getSeasonalities()).isEmpty();
    }
  }
}
