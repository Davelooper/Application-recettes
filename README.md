## Application de recettes (Full Stack)

Application de gestion de recettes.

## Statut

- Backend : fondations en place (modèle de données, persistance, migrations DB, validation, mapping, tests).
- API REST : en cours de développement.
- Frontend : à venir (Angular) une fois l’API stabilisée.

## Fonctionnalités déjà en place (backend)

- Modèle de données : recettes, ingrédients, utilisateurs, relations recette↔ingrédients.
- Migrations PostgreSQL versionnées avec Liquibase.
- Validation automatique des requêtes (Jakarta Validation) + règles personnalisées (ex: confirmation du mot de passe).
- Mapping Entités/DTO via MapStruct.
- Tests unitaires + tests d’intégration DB avec Testcontainers.

## Architecture

Monorepo :

- `backend/` : Spring Boot 4.0.3 (Java 25), API + persistance.
- `frontend/` : prévu (Angular).

## Documentation API (Swagger / OpenAPI)

Swagger UI est fourni via `springdoc-openapi`.

- Swagger UI : http://localhost:8080/swagger-ui.html
- Spéc OpenAPI (JSON) : http://localhost:8080/api-docs

## Démarrage rapide (Docker — recommandé pour tester)

Ce mode construit l’application dans l’image Docker (aucune compilation locale requise).

```bash
docker compose -f docker-compose.yml up --build
```

Arrêt :

```bash
docker compose -f docker-compose.yml down
```

## Mode développement (Docker)

Le Makefile utilise `docker-compose.dev.yml` et monte le code en volume. Ce mode suppose que `backend/target/classes` et `backend/target/lib` existent déjà (voir `CHEATSHEET.md`).

```bash
make up        # Lance la base de données et l'API (dev)
make logs      # Suit les logs en temps réel
make down      # Stoppe et supprime les conteneurs
```

Test rapide :

```bash
curl http://localhost:8080/
```

## Développement / Tests (local)

Les commandes Maven sont récapitulées dans `CHEATSHEET.md`.

```bash
cd backend

./mvnw test              # Lance tous les tests
./mvnw verify            # Tests + checkstyle + vérifications qualité
./mvnw spotless:apply    # Formatage (Google Java Format)
./mvnw spring-boot:run   # Lance l'API hors Docker
```