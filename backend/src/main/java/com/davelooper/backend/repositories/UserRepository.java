package com.davelooper.backend.repositories;

import com.davelooper.backend.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  // Spring va générer automatiquement : SELECT COUNT(*) FROM users WHERE email = ? > 0
  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);
}
