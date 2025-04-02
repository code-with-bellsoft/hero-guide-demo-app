# Use Java 23 as the base image
FROM bellsoft/liberica-runtime-container:jdk

# Install curl for healthchecks
RUN apk add curl

# Set working directory
WORKDIR /app

# Copy the Maven wrapper, parent pom.xml, and module files
COPY .mvn/ .mvn/
COPY mvnw ./
COPY pom.xml ./
COPY chat-api/pom.xml ./chat-api/
COPY chat-api/src/ ./chat-api/src/
COPY bot-assistant/pom.xml ./bot-assistant/pom.xml

# Build the application
RUN ./mvnw package -pl chat-api -am -DskipTests

# Expose the port the app runs on
EXPOSE 8080

# Run the application with AppCDS
ENTRYPOINT ["java", "-jar", "chat-api/target/chat-api-1.0-SNAPSHOT.jar"]
