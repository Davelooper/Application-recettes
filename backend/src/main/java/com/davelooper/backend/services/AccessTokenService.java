package com.davelooper.backend.services;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.davelooper.backend.entities.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccessTokenService {
  @Value("${security.jwt.access-token-secret}")
  private String accessTokenSecret;

  @Value("${security.jwt.access-token-expiration}")
  private long accessTokenExpirationMs;

  @Value("${security.jwt.issuer:application-recettes-backend}")
  private String issuer;

  @Value("${security.jwt.audience:application-recettes-spa}")
  private String audience;

  public String generateToken(User user) {
    Instant now = Instant.now();
    Instant expiration = now.plusMillis(accessTokenExpirationMs);

    return Jwts.builder()
        .subject(String.valueOf(user.getId()))
        .issuer(issuer)
        .audience().add(audience).and()
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiration))
        .id(UUID.randomUUID().toString())
        .claim("role", user.getRole().name())
        .signWith(Keys.hmacShaKeyFor(accessTokenSecret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
        .compact();
  }
}
