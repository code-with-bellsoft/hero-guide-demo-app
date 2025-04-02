# Hero Guide Demo App

A real-time chat application with AI-powered bot assistance.

## Project Structure

The project consists of two main modules:

1. **chat-api**: Handles WebSocket connections, chat sessions, and user management with MongoDB storage
2. **bot-assistant**: Provides AI-powered responses using OpenAI API with Redis caching

## Features

- Real-time chat using WebSockets
- User authentication and authorization
- Chat session management
- AI-powered bot assistance
- Message persistence in MongoDB
- Response caching in Redis
- Containerized deployment with Docker

## Prerequisites

- Java 23
- Docker and Docker Compose
- OpenAI API key

## Running the Application

### Using Docker Compose

1. Set your OpenAI API key as an environment variable:
   ```bash
   export OPENAI_API_KEY=your-api-key-here
   ```

2. Build and start the containers:
   ```bash
   docker-compose up -d
   ```

3. Access the application:
    - Chat API: http://localhost:8080
    - Bot Assistant: http://localhost:8081

### Running Locally

1. Start MongoDB and Redis:
   ```bash
   docker-compose up -d mongodb redis
   ```

2. Set your OpenAI API key as an environment variable:
   ```bash
   export OPENAI_API_KEY=your-api-key-here
   ```

3. Build and run the chat-api module:
   ```bash
   cd chat-api
   ./mvnw spring-boot:run
   ```

4. Build and run the bot-assistant module:
   ```bash
   cd bot-assistant
   ./mvnw spring-boot:run
   ```

## API Endpoints

### Chat API

- WebSocket: `/ws`
- Chat Sessions: `/api/sessions`
- Chat History: `/api/chat/history`
- User Management: `/api/users`

### Bot Assistant

- Process Message: `/api/bot/process`
- Statistics: `/api/bot/stats`
- Cache Management: `/api/bot/cache/clear`, `/api/bot/cache/hit-ratio`
- Health Check: `/api/bot/health`

## Performance Optimizations

The application includes the following performance optimizations:

- Application Class Data Sharing (AppCDS) for improved startup time
- Redis caching for frequently asked questions
- Asynchronous message processing

## Security

- Spring Security for authentication and authorization
- Method-level security with @PreAuthorize annotations
- Password encryption with BCrypt

## Monitoring

- Spring Boot Actuator endpoints for monitoring
- Health, info, and metrics endpoints

## Recent Changes

The following improvements were made to the application:

### Authentication and User Management

- Implemented form-based authentication with 3 default users (user1/pass1, user2/pass2, user3/pass3, admin1/adminpass1)
- Added automatic session creation for authenticated users
- Fixed user session management to properly associate users with their chat sessions

### WebSocket Communication

- Fixed WebSocket connection handling for authenticated users
- Improved connect/disconnect button functionality
- Ensured proper session ID usage in WebSocket communication
- Added test to verify WebSocket message handling and bot responses

### Configuration

- Updated bot.assistant.url property to use environment variable with sensible default
- Fixed HTML structure in templates
- Improved error handling in WebSocket communication

### Bug Fixes

- Fixed issue where Optional<ChatSession> was always empty in ChatWebSocketController
- Fixed session creation logic to ensure users always have a valid session
- Fixed inactive disconnect button
- Fixed connect button not working after authentication
