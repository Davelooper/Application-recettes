package com.davelooper.backend.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.davelooper.backend.entities.Recette;

@Repository
public interface RecetteRepository extends JpaRepository<Recette, Long> {

  List<Recette> findByUtilisateurId(Long utilisateurId);

  List<Recette> findByTitreContainingIgnoreCase(String titre);
}
