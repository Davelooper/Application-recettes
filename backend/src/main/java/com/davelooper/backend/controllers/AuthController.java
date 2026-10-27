package com.davelooper.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davelooper.backend.dtos.LoginRequestDTO;
import com.davelooper.backend.dtos.LoginResponseDTO;
import com.davelooper.backend.dtos.RefreshRequestDTO;
import com.davelooper.backend.dtos.RefreshResponseDTO;
import com.davelooper.backend.dtos.RegisterRequestDTO;
import com.davelooper.backend.dtos.RegisterResponseDTO;
import com.davelooper.backend.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/login")
  public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
    return authService.login(request);
  }

  @PostMapping("/register")
  public ResponseEntity<RegisterResponseDTO> register(
      @Valid @RequestBody RegisterRequestDTO request) {
    RegisterResponseDTO response = authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/refresh")
  public RefreshResponseDTO refresh(
      @Valid @RequestBody RefreshRequestDTO request) {
    return authService.refresh(request);
  }
}
