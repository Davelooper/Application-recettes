package com.davelooper.backend.exceptions;

public class InvalidRegistrationException extends RuntimeException {
  public InvalidRegistrationException(String message) {
    super(message);
  }
}
