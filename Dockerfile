# Use Java 17 as base image
FROM openjdk:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy the actual jar file and rename to app.jar
COPY target/Notification-System-0.0.1-SNAPSHOT.jar app.jar

# Expose port (optional, for clarity)
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]