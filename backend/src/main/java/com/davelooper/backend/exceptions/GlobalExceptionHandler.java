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
    List<Map<String, String>> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> Map.of(
            "field", error.getField(),
            "message", error.getDefaultMessage()))
        .toList();

    return Map.of(
        "status", 400,
        "error", "Validation failed",
        "errors", errors);
  }
}
