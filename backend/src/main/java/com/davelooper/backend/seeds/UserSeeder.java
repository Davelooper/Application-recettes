package com.davelooper.backend.seeds;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.davelooper.backend.entities.User;
import com.davelooper.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

@Component
@RequiredArgsConstructor
public class UserSeeder implements Seeder {

  private final UserRepository userRepository;
  private final Faker faker = new Faker(new Locale("fr"));

  @Override
  public void seed() {
    if (userRepository.count() == 0) {
      System.out.println(">> Seeding Users...");

      // 1. Création d'un compte admin fixe pour le développement
      // Note : En attendant Spring Security, on stocke le texte en clair
      userRepository.save(User.builder()
          .email("admin@test.com")
          .username("admin")
          .passwordHash("admin123")
          .role(User.Role.ADMIN)
          .build());

      // 2. Création de 10 utilisateurs aléatoires
      List<User> randomUsers = IntStream.range(0, 10).mapToObj(i -> User.builder()
          .email(faker.internet().emailAddress())
          .username(faker.internet().username())
          .passwordHash("password_hash_" + i)
          .role(User.Role.STANDARD)
          .build()).collect(Collectors.toList());

      userRepository.saveAll(randomUsers);
    }
  }

  @Override
  public int getOrder() {
    return 3; // On le met en dernier, il n'a pas de dépendances directes
  }
}
