package com.davelooper.backend.seeds;

import java.util.Comparator;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DatabaseSeeder {

  @Bean
  CommandLineRunner initDatabase(List<Seeder> seeders) {
    return args -> {
      System.out.println(">> Starting database seeding...");

      seeders.stream()
          .sorted(Comparator.comparingInt(Seeder::getOrder))
          .forEach(Seeder::seed);

      System.out.println(">> Database seeding completed!");
    };
  }
}