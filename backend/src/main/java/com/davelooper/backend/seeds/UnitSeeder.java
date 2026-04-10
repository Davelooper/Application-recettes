package com.davelooper.backend.seeds;

import com.davelooper.backend.entities.Unit;
import com.davelooper.backend.repositories.UnitRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnitSeeder implements Seeder {

  private final UnitRepository unitRepository;

  @Override
  public void seed() {
    if (unitRepository.count() == 0) {
      System.out.println(">> Seeding Units...");

      // Liste exhaustive des unités courantes en cuisine
      List<String> unitNames =
          List.of(
              "g",
              "kg",
              "ml",
              "cl",
              "L",
              "cuillère(s) à soupe",
              "cuillère(s) à café",
              "pincée(s)",
              "poignée(s)",
              "filet(s)",
              "unité(s)",
              "gousse(s)",
              "tranche(s)",
              "verre(s)",
              "tasse(s)",
              "botte(s)",
              "branche(s)");

      // On insère chaque unité en base
      unitNames.forEach(
          name -> {
            unitRepository.save(Unit.builder().name(name).build());
          });
    }
  }

  @Override
  public int getOrder() {
    return 1; // Pas de dépendance, on l'exécute très tôt (en même temps que les Saisons)
  }
}
