## Application de recettes (Full Stack)

Application de gestion de recettes.

## Statut

- Backend : fondations en place (modele de donnees, persistance, migrations DB, validation, mapping, tests).
- API REST : en cours de developpement.
- Frontend : a venir (Angular) une fois l'API stabilisee.

## Fonctionnalites deja en place (backend)

- Modele de donnees : recettes, ingredients, utilisateurs, relations recette<->ingredients.
- Migrations PostgreSQL versionnees avec Liquibase.
- Validation automatique des requetes (Jakarta Validation) + regles personnalisees.
- Mapping Entites/DTO via MapStruct.
- Tests unitaires + tests d'integration DB avec Testcontainers.

## Architecture

Monorepo :

- `backend/` : Spring Boot 4.0.3 (Java 25), API + persistance.
- `frontend/` : prevu (Angular).

## Documentation API (Swagger / OpenAPI)

Swagger UI est fourni via `springdoc-openapi`.

- Swagger UI : http://localhost:8080/swagger-ui.html
- Spec OpenAPI (JSON) : http://localhost:8080/api-docs

## Demarrage rapide (mode demo, utile à toute personne voulant tester l'application)

Ce mode est versionne, pret a l'emploi, et charge automatiquement des fixtures via le profil Spring `demo`.
Il est pense pour parcourir rapidement l'application sans manipuler de secrets locaux.

```bash
docker compose -f docker-compose.demo.yml up --build -d
```

Arret :

```bash
docker compose -f docker-compose.demo.yml down
```

## Mode developpement (Docker)

Le mode dev charge `.env.dev`, monte le code en volume et garde l'API dans un flux confortable pour travailler.
Ce mode suppose que `backend/target/classes` et `backend/target/lib` existent deja (voir `CHEATSHEET.md`).

```bash
make up
make logs
make down
```

## Mode prod-like / deploiement

Le fichier `docker-compose.yml` reste reserve a un usage prod-like, alimente par `.env.prod` non versionne.

```bash
docker compose --env-file .env.prod -f docker-compose.yml up --build -d
```

## Developpement / Tests (local)

Les commandes Maven sont recapitulees dans `CHEATSHEET.md`.

```bash
cd backend

./mvnw test
./mvnw verify
./mvnw spotless:apply
./mvnw spring-boot:run
```
