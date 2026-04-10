package com.davelooper.backend.repositories;

import com.davelooper.backend.entities.Seasonality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonalityRepository extends JpaRepository<Seasonality, Long> {}
