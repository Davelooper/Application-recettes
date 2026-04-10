package com.davelooper.backend.seeds;

import com.davelooper.backend.entities.Seasonality;
import com.davelooper.backend.repositories.SeasonalityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeasonalitySeeder implements Seeder {
  private final SeasonalityRepository repository;

  @Override
  public void seed() {
    if (repository.count() == 0) {
      repository.saveAll(
          List.of(
              Seasonality.builder().periodName("Printemps").startMonth(3).endMonth(5).build(),
              Seasonality.builder().periodName("Été").startMonth(6).endMonth(8).build(),
              Seasonality.builder().periodName("Automne").startMonth(9).endMonth(11).build(),
              Seasonality.builder().periodName("Hiver").startMonth(12).endMonth(2).build()));
    }
  }

  @Override
  public int getOrder() {
    return 1;
  }
}
