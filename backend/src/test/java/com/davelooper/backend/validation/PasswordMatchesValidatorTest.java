package com.davelooper.backend.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.davelooper.backend.dtos.UserRequestDTO;

class PasswordMatchesValidatorTest {

    private PasswordMatchesValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordMatchesValidator();
    }

    @Test
    @DisplayName("Doit être valide quand les mots de passe sont identiques")
    void shouldBeValidWhenPasswordsMatch() {
        // GIVEN
        UserRequestDTO dto = new UserRequestDTO("test@mail.fr", "UserTest", "secret123", "secret123");

        // WHEN
        boolean isValid = validator.isValid(dto, null); // On passe null car on n'utilise pas le ConstraintValidatorContext

        // THEN
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Ne doit pas être valide quand les mots de passe sont différents")
    void shouldBeInvalidWhenPasswordsDoNotMatch() {
        // GIVEN
        UserRequestDTO dto = new UserRequestDTO("test@mail.fr", "UserTest", "secret123", "motdepassedifferent");

        // WHEN
        boolean isValid = validator.isValid(dto, null);

        // THEN
        assertThat(isValid).isFalse();
    }
}
