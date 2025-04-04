#
# Copyright 2025 BellSoft (info@bell-sw.com)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

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
COPY bot-assistant/pom.xml ./bot-assistant/pom.xml
COPY chat-api/src/ ./chat-api/src/

# Build the application
RUN ./mvnw package -pl chat-api -am -Dmaven.test.skip=true

# Expose the port the app runs on
EXPOSE 8080

# Run the application with AppCDS
ENTRYPOINT ["java", "-jar", "chat-api/target/chat-api-1.0-SNAPSHOT.jar"]
