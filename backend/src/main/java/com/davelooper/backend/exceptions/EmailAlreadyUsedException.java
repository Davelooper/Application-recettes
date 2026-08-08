package com.davelooper.backend.exceptions;

public class EmailAlreadyUsedException extends RuntimeException {

  public EmailAlreadyUsedException() {
    super("Cet email est déjà utilisé.");
  }
}
