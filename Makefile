# Variables
DC_FILE=docker-compose.dev.yml
DC=docker compose -f $(DC_FILE)
BACKEND_DIR=backend

# Couleurs pour l'affichage
YELLOW=\033[1;33m
GREEN=\033[1;32m
RESET=\033[0m

# Note : Si vous utilisez le Dev Container VS Code "intégré" (Docker Compose),
# le cycle de vie des conteneurs est géré par VS Code directement.
# Les commandes 'up' et 'down' ci-dessous sont utiles si vous travaillez
# en dehors du Dev Container ou si vous n'utilisez pas l'extension Dev Containers.

.PHONY: help up down stop restart logs deps compile build-all shell clean db-tables db-shell db-console

help: ## Affiche cette aide
	@echo "$(YELLOW)Commandes disponibles pour le projet :$(RESET)"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

# deps: ## 1. Installe les dépendances (Jars) dans target/lib (Nécessaire au premier lancement)
# 	@echo "$(YELLOW)Copie des dépendances Maven...$(RESET)"
# 	cd $(BACKEND_DIR) && ./mvnw dependency:copy-dependencies -DoutputDirectory=target/lib

# compile: ## 2. Compile le code Java (Nécessaire avant de lancer/redémarrer)
# 	@echo "$(YELLOW)Compilation du projet...$(RESET)"
# 	cd $(BACKEND_DIR) && ./mvnw compile

up: ## 3. Démarre l'environnement (base de données + API) en arrière-plan
	@echo "$(GREEN)Démarrage des conteneurs...$(RESET)"
	$(DC) up -d

logs: ## Affiche les logs de l'API en direct
	$(DC) logs -f api

# restart: compile ## Recompile et redémarre uniquement l'API (pour prendre en compte les changements de code)
# 	@echo "$(YELLOW)Redémarrage de l'API...$(RESET)"
# 	$(DC) restart api

down: ## Arrête et supprime les conteneurs
	$(DC) down

clean: ## Nettoie le dossier target (supprime les compilations)
	cd $(BACKEND_DIR) && ./mvnw clean

shell: ## Ouvre un terminal Bash à l'intérieur du conteneur API
	$(DC) exec api /bin/bash

db-tables: ## Liste les tables de la base de données
	@echo "$(YELLOW)Tables dans la base de données recette_db :$(RESET)"
	$(DC) exec db psql -U user -d recette_db -c '\dt'

db-shell: ## Ouvre un terminal Bash à l'intérieur du conteneur de base de données
	$(DC) exec db bash

db-console: ## Connecte au client PostgreSQL (psql) dans le conteneur
	$(DC) exec db psql -U user -d recette_db
