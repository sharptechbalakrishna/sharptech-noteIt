# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the packaged Spring Boot JAR file into the container at /app
COPY target/your-application-3.2.2.jar /app/app.jar

# Expose port 8080
EXPOSE 8080

# Define the command to run your Spring Boot application when the container starts
CMD ["java", "-jar", "app.jar"]
