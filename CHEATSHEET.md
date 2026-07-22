# Aide-Mémoire (Cheat Sheet) des Commandes de Développement

Ce document recense les commandes essentielles pour développer sur le projet.

---

## ☕ Commandes Java / Maven

Ces commandes sont à exécuter dans un terminal **à la racine du projet**.
*(Elles utilisent le wrapper `./mvnw` situé dans le dossier `backend/`)*

| Action | Commande (Bash) | Description |
| :--- | :--- | :--- |
| **Mettre à jour les dépendances** | `cd backend && ./mvnw dependency:copy-dependencies -DoutputDirectory=target/lib` | Copie les jars dans `target/lib`. Indispensable après modif de `pom.xml`. |
| **Compiler le code** | `cd backend && ./mvnw compile` | Compile uniquement les sources `.java` (rapide). Utile pour le Hot Reload. |
| **Nettoyer le projet** | `cd backend && ./mvnw clean` | Supprime le dossier `target/`. |
| **Lancer les tests** | `cd backend && ./mvnw test` | Exécute tous les tests unitaires et de repository. |
| **Lancer un test spécifique** | `cd backend && ./mvnw test -Dtest=NomDuFichierTest` | Exécute uniquement les tests d'une classe (ex: `RecipeStepRepositoryTest`). |
| **Packager l'application** | `cd backend && ./mvnw package -DskipTests` | Crée le JAR final exécutable. |
| **Vérifier le code (QA)** | `cd backend && ./mvnw verify` | Lance tests + checkstyle + formatage. |
| **Formater le code** | `cd backend && ./mvnw spotless:apply` | Applique le formatage automatique (Google Java Format). |
| **Lancer en local (Sans Docker)** | `cd backend && ./mvnw spring-boot:run` | Lance l'application directement sur votre machine/conteneur. |

---

## 🐳 Commandes Docker (Makefile)

Ces commandes contrôlent l'infrastructure (Base de données, API Conteneurisée).
Elles servent à administrer les services Docker, notamment pour redémarrer un service voisin.

| Action | Commande | Description |
| :--- | :--- | :--- |
| **Démarrer l'environnement** | `make up` | Lance la DB et l'API en arrière-plan. |
| **Arrêter l'environnement** | `make down` | Arrête et supprime les conteneurs. |
| **Voir les logs** | `make logs` | Affiche les logs de l'API en temps réel. |
| **Redémarrer l'API** | `make restart` | Redémarre le conteneur `api` (utile après une compile manuelle). |
| **Connexion DB (SQL)** | `make db-console` | Ouvre un client `psql` connecté à la base de données. |
| **Lister les tables** | `make db-tables` | Affiche la liste des tables de la base de données. |
| **Shell dans la DB** | `make db-shell` | Ouvre un terminal Bash dans le conteneur `db`. |
| **Shell dans l'API** | `make shell` | Ouvre un terminal Bash dans le conteneur `api`. |
