# Environnement de Développement Backend

Ce dossier contient le backend Spring Boot de l'application. 
L'environnement de développement Docker est conçu pour utiliser directement les fichiers compilés sur votre machine hôte ("Hot Reload"), quel que soit votre éditeur de code (VS Code, IntelliJ, Eclipse, etc.).

## 🚀 Comment ça marche ?

Le conteneur Docker de développement (`Dockerfile.dev`) ne compile pas le code lui-même. Au lieu de cela :
1. **Dossier monté** : Le dossier `backend` actuel est monté dans le conteneur (volume Docker).
2. **Compilation locale** : Votre IDE ou votre commande Maven compile les fichiers `.java` en `.class` dans le dossier local `target/classes`.
3. **Exécution** : Le conteneur exécute directement ces classes compilées.

Cela permet un cycle de développement très rapide sans avoir à reconstruire l'image Docker à chaque modification de code.

## 🛠️ Pré-requis (Installation initiale)

Avant de lancer le conteneur, vous devez préparer les fichiers nécessaires sur votre machine.

### 1. Télécharger les dépendances
Le conteneur a besoin des bibliothèques Spring Boot (fichiers `.jar`) dans un dossier spécifique.

**Exécutez cette commande une seule fois** (ou à chaque modification du `pom.xml`) :

```bash
# Depuis le dossier /backend
./mvnw dependency:copy-dependencies -DoutputDirectory=target/lib
```

### 2. Compiler le projet
Le conteneur s'attend à trouver les classes compilées dans `target/classes`.

*   **Avec un IDE (VS Code, IntelliJ, Eclipse...)** : Assurez-vous que votre IDE compile automatiquement le projet (souvent via "Build Project" ou "Auto-build") et que la sortie est dirigée vers `target/classes` (configuration standard Maven).
*   **En ligne de commande** : Si vous n'utilisez pas d'IDE, lancez `mvn compile`.

## ▶️ Lancer l'application

Une fois les dépendances copiées et le projet compilé, lancez simplement Docker Compose depuis la racine du projet :

```bash
# Depuis la racine /workspaces/Application-recettes
docker compose -f docker-compose.dev.yml up
```

## 🔄 Cycle de développement

1. **Modifiez un fichier `.java`** dans votre éditeur préféré.
2. **Compilez** (Sauvegardez si votre IDE compile à la volée, ou lancez `mvn compile`).
3. Le conteneur détecte les changements via le volume monté.
4. Si *Spring Boot DevTools* est actif, l'application redémarre automatiquement. Sinon, redémarrez le conteneur manuellement.

*Note : Si vous ajoutez une nouvelle dépendance Maven dans `pom.xml`, n'oubliez pas de relancer la commande de copie des dépendances (voir section Pré-requis).*
