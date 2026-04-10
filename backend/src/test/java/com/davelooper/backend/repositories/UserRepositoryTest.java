package com.davelooper.backend.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.davelooper.backend.entities.User;
import java.time.LocalDateTime;
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

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Tests du Repository User")
class UserRepositoryTest {

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

  @Autowired private UserRepository userRepository;

  @Test
  @DisplayName("Doit enregistrer un utilisateur complet avec les valeurs par défaut")
  void shouldSaveFullUser() {
    User user =
        User.builder()
            .email("test@example.com")
            .passwordHash("hashed_password_123")
            .username("JohnDoe")
            .build();

    User saved = userRepository.saveAndFlush(user);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getRole()).isEqualTo("standard"); // Vérifie le @Builder.Default
    assertThat(saved.getCreatedAt()).isNotNull(); // Vérifie le @CreationTimestamp
  }

  @Nested
  @DisplayName("Tests des contraintes d'intégrité")
  class ConstraintTests {

    @Test
    @DisplayName("Doit échouer si l'email est déjà utilisé")
    void shouldFailWhenEmailIsNotUnique() {
      userRepository.saveAndFlush(User.builder().email("dup@ex.com").passwordHash("h1").build());

      User duplicate = User.builder().email("dup@ex.com").passwordHash("h2").build();

      assertThatThrownBy(() -> userRepository.saveAndFlush(duplicate))
          .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Doit échouer si le passwordHash est null")
    void shouldFailWhenPasswordIsNull() {
      User user = User.builder().email("test@ex.com").build();

      assertThatThrownBy(() -> userRepository.saveAndFlush(user))
          .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Doit échouer si le username dépasse 100 caractères")
    void shouldFailWhenUsernameIsTooLong() {
      String longName = "a".repeat(101);
      User user = User.builder().email("long@ex.com").passwordHash("h1").username(longName).build();

      assertThatThrownBy(() -> userRepository.saveAndFlush(user))
          .isInstanceOf(DataIntegrityViolationException.class);
    }
  }

  @Nested
  @DisplayName("Tests de l'audit et de l'immuabilité")
  class AuditTests {

    @Test
    @DisplayName("createdAt ne doit pas être mis à jour lors d'un update")
    void shouldNotUpdateCreatedAt() throws InterruptedException {
      // 1. Sauvegarde initiale
      User user =
          userRepository.saveAndFlush(
              User.builder().email("audit@ex.com").passwordHash("h1").build());

      LocalDateTime originalDate = user.getCreatedAt();

      // 2. Petite pause pour garantir que si la date changeait, on le verrait
      Thread.sleep(10);

      // 3. Update de l'username
      user.setUsername("NewName");
      User updated = userRepository.saveAndFlush(user);

      // 4. Vérification : la date ne doit pas avoir bougé
      assertThat(updated.getCreatedAt()).isEqualTo(originalDate);
    }

    @Test
    @DisplayName("passwordHash en TEXT doit accepter de très longues chaînes")
    void shouldAcceptVeryLongPasswordHash() {
      String veryLongHash = "h".repeat(3000);
      User user =
          userRepository.saveAndFlush(
              User.builder().email("longhash@ex.com").passwordHash(veryLongHash).build());

      assertThat(userRepository.findById(user.getId()).get().getPasswordHash().length())
          .isEqualTo(3000);
    }
  }
}
