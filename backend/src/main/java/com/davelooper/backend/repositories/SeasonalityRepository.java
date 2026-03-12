package com.davelooper.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.davelooper.backend.entities.Seasonality;

@Repository
public interface SeasonalityRepository extends JpaRepository<Seasonality, Long> {
}
