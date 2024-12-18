# Étape 1 : Partir d'une image Maven existante
FROM maven:3.9.4-eclipse-temurin-11

# Étape 2 : Définir le répertoire de travail
WORKDIR /app

# Étape 3 : Copier le fichier POM pour résoudre les dépendances
COPY pom.xml .

# Étape 4 : Télécharger toutes les dépendances Maven
RUN mvn dependency:resolve

# Étape 5 : Copier tout le code source pour être prêt à compiler
COPY . .

# Étape 6 : Construire le projet
RUN mvn clean compile

