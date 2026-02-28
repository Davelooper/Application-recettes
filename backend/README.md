# Environnement de Développement Backend (DevContainer)

Ce dossier contient le backend Spring Boot de l'application. 
L'environnement est conçu pour fonctionner de manière fluide avec **VS Code** et ses **DevContainers**.

## 🚀 Comment ça marche ?

Dans ce DevContainer :
1. **VS Code compile automatiquement** vos fichiers `.java` à chaque sauvegarde.
2. Les fichiers compilés (`.class`) sont placés dans le dossier `target/classes`.
3. Le `Dockerfile.dev` monte ce dossier `target` directement dans le conteneur Docker.

Cela permet un cycle de développement très rapide sans avoir à reconstruire l'image Docker à chaque modification de code.

## 🛠️ Pré-requis (Installation initiale)

Le conteneur Docker a besoin des bibliothèques Spring Boot (les `.jar` de dépendances) pour démarrer. VS Code ne les place pas automatiquement au bon endroit pour le Dockerfile.

**Vous devez exécuter cette commande une seule fois** (ou à chaque fois que vous modifiez le `pom.xml`) :

```bash
# Depuis le dossier /backend
./mvnw dependency:copy-dependencies -DoutputDirectory=target/lib
```

## ▶️ Lancer l'application

Une fois les dépendances copiées, lancez simplement Docker Compose depuis la racine du projet :

```bash
# Depuis la racine /workspaces/Application-recettes
docker compose -f docker-compose.dev.yml up
```

## 🔄 Cycle de développement

1. **Modifiez un fichier `.java`** dans VS Code.
2. **Sauvegardez**.
3. VS Code recompile instantanément le fichier dans `target/classes`.
4. Si vous avez *Spring Boot DevTools*, l'application redémarre automatiquement. Sinon, redémarrez simplement le conteneur.

*Note : Si vous ajoutez une nouvelle dépendance Maven dans `pom.xml`, n'oubliez pas de relancer la commande de copie des dépendances (voir section Pré-requis).*
