package com.davelooper.backend.repositories;

import com.davelooper.backend.entities.RefreshToken;
import com.davelooper.backend.entities.User;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByTokenHash(String tokenHash);

  @Modifying
  @Query(
      "UPDATE RefreshToken r SET r.revokedAt = :revokedAt WHERE r.user = :user AND r.revokedAt IS NULL")
  void revokeAllByUser(@Param("user") User user, @Param("revokedAt") LocalDateTime revokedAt);

  @Modifying
  @Query("DELETE FROM RefreshToken r WHERE r.expiresAt < :now")
  int deleteAllExpiredBefore(@Param("now") LocalDateTime now);
}
