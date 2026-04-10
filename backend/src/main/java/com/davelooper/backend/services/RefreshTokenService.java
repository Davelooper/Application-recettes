package com.davelooper.backend.services;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
   public String generateToken() {
    return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(new java.security.SecureRandom().generateSeed(64));
   }
}
