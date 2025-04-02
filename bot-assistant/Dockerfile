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
COPY bot-assistant/pom.xml ./bot-assistant/
COPY chat-api/pom.xml ./chat-api/pom.xml
COPY bot-assistant/src/ ./bot-assistant/src/

# Build the application
RUN ./mvnw package -pl bot-assistant -am -DskipTests

# Expose the port the app runs on
EXPOSE 8081

# Run the application with AppCDS
CMD ["java", "-jar", "bot-assistant/target/bot-assistant-1.0-SNAPSHOT.jar"]
