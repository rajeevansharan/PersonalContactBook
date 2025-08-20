# Use official OpenJDK 21 slim image
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the built jar from target folder into the container
COPY target/Personal-Contact-Book-0.0.1-SNAPSHOT.jar app.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]