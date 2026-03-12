package com.davelooper.backend.entities;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "utilisateur")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "mot_de_passe_hash", nullable = false, columnDefinition = "TEXT")
  private String motDePasseHash;

  @Column(name = "nom_utilisateur", length = 100)
  private String nomUtilisateur;

  @Column(name = "role", nullable = false, length = 50)
  @Builder.Default
  private String role = "standard";

  @Column(name = "date_creation", updatable = false)
  @CreationTimestamp
  private LocalDateTime dateCreation;
}
