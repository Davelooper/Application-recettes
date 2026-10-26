package com.davelooper.backend.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.davelooper.backend.dtos.RegisterRequestDTO;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

class PasswordMatchesValidatorTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  @DisplayName("Doit être valide quand les mots de passe sont identiques")
  void shouldBeValidWhenPasswordsMatch() {
    // GIVEN
    RegisterRequestDTO dto =
        new RegisterRequestDTO("test@mail.fr", "UserTest", "secret123", "secret123");

    // WHEN
    boolean isValid = validator.validate(dto).isEmpty();

    // THEN
    assertThat(isValid).isTrue();
  }

  @Test
  @DisplayName("Ne doit pas être valide quand les mots de passe sont différents")
  void shouldBeInvalidWhenPasswordsDoNotMatch() {
    // GIVEN
    RegisterRequestDTO dto =
        new RegisterRequestDTO("test@mail.fr", "UserTest", "secret123", "motdepassedifferent");

    // WHEN
    var violations = validator.validate(dto);

    // THEN
    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getPropertyPath().toString())
      .isEqualTo("passwordConfirm");
    assertThat(violations.iterator().next().getMessage())
      .isEqualTo("Les deux mots de passe ne correspondent pas");
  }
}
