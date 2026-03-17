package com.davelooper.backend.validation;

import com.davelooper.backend.dtos.UserRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserRequestDTO> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserRequestDTO dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true; 
        }
        
        // Si les deux sont nuls en même temps, ce n'est pas ce validateur qui doit bloquer (c'est @NotBlank)
        if (dto.password() == null || dto.passwordConfirm() == null) {
            return false;
        }

        return dto.password().equals(dto.passwordConfirm());
    }
}
