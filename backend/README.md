# Environnement de developpement backend

Ce dossier contient le backend Spring Boot de l'application.
L'environnement de developpement Docker est concu pour utiliser directement les fichiers compiles sur votre machine hote, quel que soit votre editeur de code (VS Code, IntelliJ, Eclipse, etc.).

## Aide-memoire des commandes

Pour une liste complete des commandes de developpement (compilation, tests, Docker, DB...), consultez [CHEATSHEET.md](/home/david/Projets_Persos/Application-recettes/CHEATSHEET.md).

## Comment ca marche ?

Le conteneur Docker de developpement (`Dockerfile.dev`) ne compile pas le code lui-meme. A la place :

1. Le dossier `backend` est monte dans le conteneur.
2. Votre IDE ou Maven compile les fichiers `.java` en `.class` dans `target/classes`.
3. Le conteneur execute directement ces classes compilees.

Cela permet un cycle de developpement rapide sans reconstruire l'image Docker a chaque modification.

## Fichier d'environnement

Le mode developpement Docker, y compris le Dev Container VS Code, utilise le fichier `.env.dev` situe a la racine du projet.
Le mode demo utilise `.env.demo` et le mode prod-like utilise `.env.prod`.

## Pre-requis

Avant de lancer le conteneur, preparez les fichiers necessaires sur votre machine.

### 1. Telecharger les dependances

Le conteneur a besoin des bibliotheques Spring Boot (`.jar`) dans un dossier specifique.

Executez cette commande une seule fois, ou a chaque modification du `pom.xml` :

```bash
./mvnw dependency:copy-dependencies -DoutputDirectory=target/lib
```

### 2. Compiler le projet

Le conteneur s'attend a trouver les classes compilees dans `target/classes`.

- Avec un IDE, assurez-vous que la compilation automatique alimente bien `target/classes`.
- En ligne de commande, lancez `./mvnw compile`.

## Lancer l'application

Une fois les dependances copiees et le projet compile, lancez Docker Compose depuis la racine du projet :

```bash
make up
```

Ou, si vous preferez la commande brute :

```bash
docker compose -f docker-compose.dev.yml up
```

## Gestion des dependances

Lorsque vous ajoutez une dependance dans le `pom.xml`, le conteneur ne la voit pas automatiquement car il utilise une copie locale des `.jar` situee dans `target/lib`.

Pour appliquer les changements :

1. Copiez les nouvelles dependances :

```bash
./mvnw dependency:copy-dependencies -DoutputDirectory=target/lib
```

2. Redemarrez le conteneur API :

```bash
make restart
```

Ou, si vous preferez la commande brute :

```bash
docker compose -f docker-compose.dev.yml restart api
```

## Documentation API (Swagger UI)

Une fois l'application lancee, la documentation OpenAPI est accessible ici :

- Swagger UI : http://localhost:8080/swagger-ui.html
- JSON OpenAPI : http://localhost:8080/api-docs

Ces chemins sont configures dans `application.properties`.

## Cycle de developpement

1. Modifiez un fichier `.java`.
2. Compilez avec votre IDE ou avec `./mvnw compile`.
3. Le conteneur detecte les changements via le volume monte.
4. Si Spring Boot DevTools est actif, l'application redemarre automatiquement. Sinon, redemarrez le conteneur manuellement.

Si vous ajoutez une nouvelle dependance Maven dans `pom.xml`, relancez aussi la copie des dependances.
