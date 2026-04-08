package com.davelooper.backend.seeds;

import java.util.Comparator;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.davelooper.backend.repositories.IngredientRepository;
import com.davelooper.backend.repositories.RecipeIngredientRepository;
import com.davelooper.backend.repositories.RecipeRepository;
import com.davelooper.backend.repositories.SeasonalityRepository;
import com.davelooper.backend.repositories.UnitRepository;
import com.davelooper.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@Profile({"dev", "demo"})
@RequiredArgsConstructor
public class DatabaseSeeder {

  private final SeasonalityRepository seasonalityRepository;
  private final UnitRepository unitRepository;
  private final IngredientRepository ingredientRepository;
  private final UserRepository userRepository;
  private final RecipeRepository recipeRepository;
  private final RecipeIngredientRepository recipeIngredientRepository;

  @Bean
  CommandLineRunner initDatabase(List<Seeder> seeders) {
    return args -> {
      boolean databaseAlreadySeeded = seasonalityRepository.count() > 0
          || unitRepository.count() > 0
          || ingredientRepository.count() > 0
          || userRepository.count() > 0
          || recipeRepository.count() > 0
          || recipeIngredientRepository.count() > 0;

      if (databaseAlreadySeeded) {
        System.out.println(">> Database already contains data. Skipping automatic seeding.");
        return;
      }

      System.out.println(">> Starting database seeding...");

      seeders.stream()
          .sorted(Comparator.comparingInt(Seeder::getOrder))
          .forEach(Seeder::seed);

      System.out.println(">> Database seeding completed!");
    };
  }
}
