# Use an OpenJDK base image
FROM maven:3.8.6-openjdk-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy project files and build the JAR
COPY . .
RUN mvn clean package -DskipTests

# Use a lightweight JDK image to run the app
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copy the built JAR file into the container
COPY --from=build /app/target/steelbulldog-1.0-SNAPSHOT.jar /app/vaadin-app.jar

# Expose the port used by the application
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "/app/vaadin-app.jar"]