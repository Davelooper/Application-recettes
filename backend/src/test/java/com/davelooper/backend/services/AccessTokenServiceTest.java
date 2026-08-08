package com.davelooper.backend.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.davelooper.backend.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class AccessTokenServiceTest {

  private AccessTokenService service;

  private static final String SECRET = "01234567890123456789012345678901";

  @BeforeEach
  void setUp() {
    service = new AccessTokenService();
    ReflectionTestUtils.setField(service, "accessTokenSecret", SECRET);
    ReflectionTestUtils.setField(service, "accessTokenExpirationMs", 60_000L);
    ReflectionTestUtils.setField(service, "issuer", "application-recettes-backend");
    ReflectionTestUtils.setField(service, "audience", "application-recettes-spa");
  }

  @Test
  void shouldGenerateTokenWithExpectedStandardClaimsAndRole() {
    User user = User.builder().id(42L).role(User.Role.ADMIN).build();

    String token = service.generateToken(user);

    SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

    assertThat(claims.getSubject()).isEqualTo("42");
    assertThat(claims.getIssuer()).isEqualTo("application-recettes-backend");
    assertThat(claims.getAudience()).contains("application-recettes-spa");
    assertThat(claims.get("role", String.class)).isEqualTo("ADMIN");
    assertThat(claims.getId()).isNotBlank();
    assertThat(claims.getIssuedAt()).isBefore(claims.getExpiration());
  }

  @Test
  void shouldGenerateDifferentJtiForTwoTokens() {
    User user = User.builder().id(42L).role(User.Role.ADMIN).build();

    String t1 = service.generateToken(user);
    String t2 = service.generateToken(user);

    SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    String jti1 = Jwts.parser().verifyWith(key).build().parseSignedClaims(t1).getPayload().getId();
    String jti2 = Jwts.parser().verifyWith(key).build().parseSignedClaims(t2).getPayload().getId();

    assertThat(jti1).isNotEqualTo(jti2);
  }
}
