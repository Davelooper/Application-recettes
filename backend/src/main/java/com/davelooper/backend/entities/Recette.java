package com.davelooper.backend.entities;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recette")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recette {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "utilisateur_id", nullable = false)
  private Utilisateur utilisateur;

  @Column(name = "titre", nullable = false)
  private String titre;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "portions")
  private Integer portions;

  @Column(name = "difficulte")
  private Integer difficulte;

  @Column(name = "image_url", columnDefinition = "TEXT")
  private String imageUrl;

  @Column(name = "temps_prep_min")
  private Integer tempsPrepMin;

  @Column(name = "temps_cuisson_min")
  private Integer tempsCuissonMin;

  @Column(name = "date_creation", updatable = false)
  @CreationTimestamp
  private LocalDateTime dateCreation;
}
