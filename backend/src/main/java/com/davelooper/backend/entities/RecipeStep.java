package com.davelooper.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recipe_steps",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"recipe_id", "step_number"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeStep {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recipe_id", nullable = false)
  private Recipe recipe;

  @Column(name = "step_number", nullable = false)
  private Integer stepNumber;

  @Column(name = "description", nullable = false, columnDefinition = "TEXT")
  private String description;
}
