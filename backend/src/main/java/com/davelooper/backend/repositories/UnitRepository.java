package com.davelooper.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.davelooper.backend.entities.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
}
