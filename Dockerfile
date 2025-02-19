# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy project files and build the JAR
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application using a JDK image
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/steelbulldog-1.0-SNAPSHOT.jar /app/vaadin-app.jar

# Copy frontend (includes themes)
COPY --from=build /app/src/main/frontend /app/src/main/frontend

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/vaadin-app.jar"]
