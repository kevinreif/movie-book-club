# Use an OpenJDK base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/steelbulldog-1.0-SNAPSHOT.jar /app/vaadin-app.jar

# Expose the port used by the application
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/vaadin-app.jar"]