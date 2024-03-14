# Stage 1: Build the Spring Boot application
FROM gradle:7.5-jdk17 AS build
WORKDIR /app

# Copy the Gradle build files
COPY build.gradle settings.gradle ./
COPY src/ src/

# Build the application (exclude tests)
RUN gradle clean build -x test

# Stage 2: Create the final image
FROM bellsoft/liberica-openjdk-debian:17.0.1
WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
