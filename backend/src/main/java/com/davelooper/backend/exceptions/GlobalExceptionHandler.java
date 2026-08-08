package com.davelooper.backend.exceptions;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
    List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> Map.of("field", error.getField(), "message", error.getDefaultMessage()))
        .toList();

    return Map.of("type", "/problems/validation", "title", "Données invalides", "status",
        HttpStatus.BAD_REQUEST.value(), "detail", "Certains champs sont invalides.", "errors",
        errors);
  }

  @ExceptionHandler(EmailAlreadyUsedException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public Map<String, Object> handleEmailAlreadyUsed(EmailAlreadyUsedException ex) {
    return Map.of("type", "/problems/email-already-used", "title", "Conflit", "status",
        HttpStatus.CONFLICT.value(), "detail", ex.getMessage(), "errors",
        List.of(Map.of("field", "email", "message", ex.getMessage())));
  }

  @ExceptionHandler(InvalidRegistrationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handleInvalidRegistration(InvalidRegistrationException ex) {
    return Map.of("type", "/problems/invalid-registration", "title", "Inscription invalide",
        "status", HttpStatus.BAD_REQUEST.value(), "detail", ex.getMessage());
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> handleInvalidCredentials(InvalidCredentialsException ex) {
    return Map.of("type", "/problems/invalid-credentials", "title", "Identifiants invalides",
        "status", HttpStatus.BAD_REQUEST.value(), "detail", ex.getMessage());
  }

}
