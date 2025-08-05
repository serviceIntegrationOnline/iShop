# Use a valid, latest JDK 17 image
FROM eclipse-temurin:17-jdk

# Metadata
LABEL maintainer="NILE"

# Set working directory
WORKDIR /app

# Copy fat JAR into image
COPY target/vertx-crud-mvc-1.0.0-fat.jar app.jar

# Expose port used by Vert.x (default 8080)
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
