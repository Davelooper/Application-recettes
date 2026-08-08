package com.davelooper.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
  public String generateToken() {
    return java.util.Base64.getUrlEncoder()
        .withoutPadding()
        .encodeToString(new java.security.SecureRandom().generateSeed(64));
  }
}
