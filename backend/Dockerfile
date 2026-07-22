# Étape 1 : Build avec Maven et JDK
FROM maven:3-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
# On compile sans lancer les tests pour gagner du temps
RUN mvn clean package -DskipTests

# Étape 2 : Image d'exécution légère
FROM eclipse-temurin:25-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
RUN mkdir -p /app/logs
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]