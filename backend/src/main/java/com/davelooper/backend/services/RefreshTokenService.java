package com.davelooper.backend.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HexFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.davelooper.backend.entities.RefreshToken;
import com.davelooper.backend.entities.User;
import com.davelooper.backend.repositories.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
  @Value("${security.jwt.refresh-token-expiration:604800000}")
  private long refreshTokenExpirationMs;

  private final RefreshTokenRepository refreshTokenRepository;

  public record GeneratedRefreshToken(String rawToken, RefreshToken entity) {
  }

  public record RotationResult(User user, String newRawToken) {
  }

  public String generateToken() {
    return java.util.Base64.getUrlEncoder()
        .withoutPadding()
        .encodeToString(new java.security.SecureRandom().generateSeed(64));
  }

  public String hashToken(String rawToken) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hashBytes);
    } catch (Exception e) {
      throw new IllegalStateException("Algorithme de hachage SHA-256 indisponible", e);
    }
  }

  @Transactional
  public GeneratedRefreshToken create(User user) {
    String token = this.generateToken();
    String hash = this.hashToken(token);
    LocalDateTime expiresAt = LocalDateTime.now().plus(Duration.ofMillis(refreshTokenExpirationMs));
    RefreshToken refreshToken = RefreshToken.builder()
        .user(user)
        .tokenHash(hash)
        .expiresAt(expiresAt)
        .build();
    RefreshToken saved = refreshTokenRepository.save(refreshToken);

    return new GeneratedRefreshToken(token, saved);
  }
}
