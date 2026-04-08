package com.davelooper.backend.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
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
import com.davelooper.backend.entities.RecipeStep;
import com.davelooper.backend.entities.User;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Tests du Repository RecipeStep")
class RecipeStepRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    @Autowired
    private RecipeStepRepository recipeStepRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    private Recipe testRecipe;

    @BeforeEach
    void setUp() {
        User user = User.builder().email("test@example.com").passwordHash("hash")
                .username("test_user").role(User.Role.STANDARD).build();
        user = userRepository.save(user);

        testRecipe = Recipe.builder().title("Test Recipe").user(user).build();
        testRecipe = recipeRepository.save(testRecipe);
    }

    @Nested
    @DisplayName("Tests de persistance de base")
    class PersistenceTests {

        @Test
        @DisplayName("Doit enregistrer une étape de recette avec tous ses champs")
        void shouldSaveRecipeStep() {
            RecipeStep step = RecipeStep.builder().recipe(testRecipe).stepNumber(1)
                    .description("Couper les légumes en dés").build();

            RecipeStep saved = recipeStepRepository.save(step);

            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getStepNumber()).isEqualTo(1);
            assertThat(saved.getDescription()).isEqualTo("Couper les légumes en dés");
            assertThat(saved.getRecipe()).isEqualTo(testRecipe);
        }

        @Test
        @DisplayName("Doit échouer si le numéro d'étape est null (Contrainte NOT NULL)")
        void shouldFailWhenStepNumberIsNull() {
            RecipeStep step =
                    RecipeStep.builder().recipe(testRecipe).description("Mélanger").build();

            assertThatThrownBy(() -> recipeStepRepository.saveAndFlush(step))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }

        @Test
        @DisplayName("Doit échouer si la description est null (Contrainte NOT NULL)")
        void shouldFailWhenDescriptionIsNull() {
            RecipeStep step = RecipeStep.builder().recipe(testRecipe).stepNumber(2).build();

            assertThatThrownBy(() -> recipeStepRepository.saveAndFlush(step))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @Nested
    @DisplayName("Tests d'unicité (Unique Constraints)")
    class ConstraintTests {

        @Test
        @DisplayName("Doit échouer si le couple [recipe_id, step_number] est déjà pris")
        void shouldFailWhenDuplicateStepNumberForSameRecipe() {
            RecipeStep step1 = RecipeStep.builder().recipe(testRecipe).stepNumber(1)
                    .description("Etape 1").build();
            recipeStepRepository.saveAndFlush(step1);

            RecipeStep step2 = RecipeStep.builder().recipe(testRecipe).stepNumber(1)
                    .description("Etape 1 bis").build();

            assertThatThrownBy(() -> recipeStepRepository.saveAndFlush(step2))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }

        @Test
        @DisplayName("Doit permettre le même numéro d'étape pour deux recettes différentes")
        void shouldAllowSameStepNumberForDifferentRecipes() {
            // Create a second recipe
            Recipe testRecipe2 =
                    Recipe.builder().title("Another Recipe").user(testRecipe.getUser()).build();
            testRecipe2 = recipeRepository.save(testRecipe2);

            RecipeStep step1 = RecipeStep.builder().recipe(testRecipe).stepNumber(1)
                    .description("Etape 1 - Recette 1").build();
            recipeStepRepository.save(step1);

            RecipeStep step2 = RecipeStep.builder().recipe(testRecipe2).stepNumber(1)
                    .description("Etape 1 - Recette 2").build();

            RecipeStep savedStep2 = recipeStepRepository.saveAndFlush(step2);
            assertThat(savedStep2.getId()).isNotNull();
        }
    }
}
