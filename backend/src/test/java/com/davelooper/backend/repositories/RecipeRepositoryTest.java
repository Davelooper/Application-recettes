package com.davelooper.backend.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import com.davelooper.backend.entities.Recipe;
import com.davelooper.backend.entities.User;
import jakarta.persistence.EntityManager;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Tests du Repository Recipe")
class RecipeRepositoryTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

  @Autowired
  private RecipeRepository recipeRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EntityManager entityManager;

  @Nested
  @DisplayName("Tests de persistance et relations")
  class PersistenceTests {

    @Test
    @DisplayName("Doit sauvegarder une recette liée à un utilisateur existant")
    void shouldSaveRecipeWithUser() {
      // Given
      User chef = userRepository.save(
          User.builder().email("chef@kitchen.com").username("Gordon").passwordHash("pwd").build());

      Recipe recipe = Recipe.builder().title("Bœuf Bourguignon").description("Mijoté lentement...")
          .servings(4).user(chef).build();

      // When
      Recipe savedRecipe = recipeRepository.save(recipe);

      // Then
      assertThat(savedRecipe.getId()).isNotNull();
      assertThat(savedRecipe.getUser().getId()).isEqualTo(chef.getId());
    }

    @Test
    @DisplayName("Doit échouer si on tente de sauvegarder une recette sans utilisateur")
    void shouldFailToSaveRecipeWithoutUser() {
      Recipe orphanRecipe = Recipe.builder().title("Recette fantôme").build();

      assertThatThrownBy(() -> recipeRepository.saveAndFlush(orphanRecipe))
          .isInstanceOf(DataIntegrityViolationException.class);
    }
  }

  @Nested
  @DisplayName("Tests de sécurité des suppressions (Cascades)")
  class CascadeTests {

    @Test
    @DisplayName("La suppression d'une recette ne doit PAS supprimer l'utilisateur")
    void shouldNotDeleteUserWhenRecipeIsDeleted() {
      // Given
      User chef = userRepository
          .save(User.builder().email("survivor@test.com").passwordHash("pwd").build());
      Recipe recipe = recipeRepository.save(Recipe.builder().title("To delete").user(chef).build());

      // When
      recipeRepository.deleteById(recipe.getId());
      recipeRepository.flush();

      // Then
      assertThat(recipeRepository.existsById(recipe.getId())).isFalse();
      assertThat(userRepository.existsById(chef.getId())).isTrue();
    }

    @Test
    @DisplayName("La suppression d'un utilisateur doit échouer s'il a encore des recettes (Protection FK)")
    void shouldFailToDeleteUserIfHasRecipes() {
      // Given: On sauvegarde l'utilisateur
      User chef = userRepository
          .saveAndFlush(User.builder().email("busy@test.com").passwordHash("pwd").build());

      // IMPORTANT: On recharge l'utilisateur pour être sûr qu'il est bien attaché à la session
      // Hibernate.
      // Cela évite l'erreur TransientPropertyValueException si 'chef' est devenu détaché.
      User managedChef = userRepository.findById(chef.getId()).orElseThrow();

      // On utilise saveAndFlush pour la recette avec l'utilisateur managé
      recipeRepository.saveAndFlush(Recipe.builder().title("My Recipe").user(managedChef).build());

      // On vide le contexte Hibernate pour éviter les vérifications d'état en mémoire
      // et tester la contrainte FK côté base de données.
      entityManager.clear();

      // When / Then: On essaie de supprimer le Chef, mais il a encore des recettes !
      // La base de données doit dire NON (DataIntegrityViolationException)
      // IMPORTANT : On doit utiliser deleteById + flush() pour forcer l'envoi du DELETE SQL
      // immédiatement
      assertThatThrownBy(() -> {
        userRepository.deleteById(chef.getId());
        userRepository.flush(); // Force la tentative de suppression en base
      }).isInstanceOf(DataIntegrityViolationException.class);
    }
  }

  @Nested
  @DisplayName("Tests des types de données spécifiques")
  class DataTypesTests {

    @Test
    @DisplayName("Doit gérer les descriptions longues (@Lob)")
    void shouldSaveAndRetrieveLargeDescription() {
      // Given
      User user =
          userRepository.save(User.builder().email("writer@test.com").passwordHash("pwd").build());

      // Création d'une description > 255 caractères (limite VARCHAR par défaut)
      String longDescription = "Texte ".repeat(1000);

      Recipe recipe =
          Recipe.builder().title("Roman culinaire").description(longDescription).user(user).build();

      // When
      Recipe saved = recipeRepository.saveAndFlush(recipe);

      // On recharge dans une nouvelle transaction pour bien tester la persistence
      Recipe fetched = recipeRepository.findById(saved.getId()).orElseThrow();

      // Then
      assertThat(fetched.getDescription()).hasSize(longDescription.length());
    }
  }
}
