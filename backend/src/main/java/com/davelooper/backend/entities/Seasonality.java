package com.davelooper.backend.entities;

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
@Table(name = "seasonality")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seasonality {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "period_name", length = 50, unique = true)
  private String periodName;

  @Column(name = "start_month", nullable = false)
  private Integer startMonth;

  @Column(name = "end_month", nullable = false)
  private Integer endMonth;
}
