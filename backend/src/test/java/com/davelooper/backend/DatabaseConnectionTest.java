package com.davelooper.backend;

import static org.assertj.core.api.Assertions.assertThat;
import java.sql.Connection;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DatabaseConnectionTest {

  // On utilise l'image postgres officielle
  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

  @Autowired
  private DataSource dataSource;

  @Test
  void testConnectionIsEstablished() throws Exception {
    // 1. Vérifie que Docker a bien lancé le conteneur
    assertThat(postgres.isRunning()).isTrue();

    // 2. Vérifie que Spring arrive à se connecter à ce conteneur précis
    try (Connection connection = dataSource.getConnection()) {
      assertThat(connection.getMetaData().getDatabaseProductName()).isEqualTo("PostgreSQL");
      assertThat(connection.isValid(1)).isTrue();
    }
  }
}
