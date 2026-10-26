package com.davelooper.backend.validation;

import com.davelooper.backend.dtos.RegisterRequestDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
    implements ConstraintValidator<PasswordMatches, RegisterRequestDTO> {

  @Override
  public void initialize(PasswordMatches constraintAnnotation) {}

  @Override
  public boolean isValid(RegisterRequestDTO dto, ConstraintValidatorContext context) {
    if (dto == null || dto.password() == null || dto.passwordConfirm() == null) {
      return true; // @NotBlank gère l'absence
    }

    if (dto.password().equals(dto.passwordConfirm())) {
      return true;
    }

    context.disableDefaultConstraintViolation();
    context
      .buildConstraintViolationWithTemplate("Les deux mots de passe ne correspondent pas")
      .addPropertyNode("passwordConfirm")
      .addConstraintViolation();

    return false;
  }
}
