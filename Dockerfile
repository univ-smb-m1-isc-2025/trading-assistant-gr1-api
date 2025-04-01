# Utiliser une image JRE légère pour exécuter l'application
FROM eclipse-temurin:17-jre-alpine

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier .jar généré dans le conteneur
COPY ../trading-assistant-gr1-api/target/api-0.0.2-SNAPSHOT.jar app.jar

# Exposer le port 8080 pour l'application
EXPOSE 8080

# Commande pour exécuter l'application
CMD ["java", "-XX:InitialRAMPercentage=50", "-XX:MaxRAMPercentage=70", "-XshowSettings", "-jar", "app.jar"]