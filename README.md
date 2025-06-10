# Chatbot System: AI-Powered Conversation Service

This project is a full-stack chatbot application with a modern React frontend and Spring Boot backend, built with Clean Architecture principles and Google Gemini AI integration.

Key features:

- **Frontend**: React + TypeScript + Material-UI with ChatGPT-like interface
- **Backend**: Spring Boot chatbot service with Clean Architecture
- **Authentication**: JWT-based user authentication system
- **AI Integration**: Google Gemini AI for intelligent responses with full context preservation
- **Context Management**: Advanced conversation context storage and retrieval system
- **Database**: PostgreSQL for data persistence and conversation history
- **Containerization**: Full Docker support with Docker Compose

---

## Requirements

- [Docker Desktop](https://www.docker.com/products/docker-desktop) installed and running
- Java 21 (for local development)
- Maven (for local development)

---

## How to Run

### Option 1: Using Docker Compose (Recommended)

In the root directory (where `docker-compose.yml` is located), run:

```bash
docker-compose up --build
```

This will:

- Build and start all services: frontend, backend, and database
- Expose ports:
  - **Frontend**: [http://localhost](http://localhost) (React app with ChatGPT-like interface)
  - **Backend API**: [http://localhost:8080](http://localhost:8080) (Spring Boot REST API)
  - **PostgreSQL**: `localhost:5432` (user: `chatbot_user`, password: `chatbot_pass`, database: `chatbot`)

### Option 2: Running Locally

First, ensure PostgreSQL is running, then:

```bash
cd chatbot
./mvnw spring-boot:run
```

The service will be available at: [http://localhost:8080](http://localhost:8080)

---

## Access Points

Once the application is running:

- **Main Application**: [http://localhost](http://localhost) - React frontend with chat interface
- **API Documentation (Swagger)**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) - Backend API docs
- **Direct Backend API**: [http://localhost:8080](http://localhost:8080) - Spring Boot REST API

### Available Endpoints

**Authentication:**
- `POST /auth/register` - Register a new user
- `POST /auth/login` - User login

**Chat Operations:**
- `POST /api/chat/create` - Create a new conversation
- `GET /api/chat/history` - Get user's conversation history
- `GET /api/chat/getConversation/{conversationId}` - Get messages from a specific conversation
- `POST /api/chat/getResponse/{conversationId}` - Send message and get AI response

---

## Features

### 🎨 Modern UI/UX
- **ChatGPT-like interface** with clean, responsive design
- **Dark sidebar** with conversation history
- **Real-time messaging** with typing indicators
- **Material Design** components throughout

### 🔐 Authentication System
- **User registration** with validation
- **Secure login** with JWT tokens
- **Protected routes** requiring authentication
- **Automatic token management**

### 💬 Advanced Chat Functionality
- **Create new conversations** with custom names
- **Browse conversation history** with timestamps
- **Real-time AI responses** powered by Google Gemini
- **Message persistence** across sessions
- **Conversation context preservation** - AI remembers entire conversation history
- **Seamless conversation switching** without losing context
- **Intelligent context management** - Each conversation maintains its own context
- **Message threading** - Related messages are grouped and contextualized

### 🏗️ Technical Features
- **Clean Architecture** with clear separation of concerns
- **Full containerization** with Docker Compose
- **Responsive design** that works on all devices
- **Type-safe development** with TypeScript
- **RESTful API** with comprehensive documentation

---

## Rebuild Services (after code changes)

### Rebuild specific service:
```bash
# Rebuild frontend only
docker-compose build chatbot-frontend

# Rebuild backend only  
docker-compose build chatbot-backend

# Rebuild all services
docker-compose build
```

### Restart services:
```bash
# Restart specific service
docker-compose up -d chatbot-frontend
docker-compose up -d chatbot-backend

# Restart all services
docker-compose up -d
```

---

## Reset Everything (Clean Build)

If you want to rebuild everything from scratch (clear cache, volumes, networks):

```bash
docker-compose down -v --rmi all --remove-orphans
docker-compose up --build
```

---

## Project Structure

```
.
├── docker-compose.yml          # Docker compose configuration
├── frontend/                   # React frontend application
│   ├── src/
│   │   ├── components/
│   │   │   ├── Auth/           # Authentication components
│   │   │   │   ├── Login.tsx
│   │   │   │   ├── Register.tsx
│   │   │   │   └── AuthPage.tsx
│   │   │   ├── Chat/           # Chat interface components
│   │   │   │   ├── ChatInterface.tsx
│   │   │   │   ├── Sidebar.tsx
│   │   │   │   ├── MessageList.tsx
│   │   │   │   └── MessageInput.tsx
│   │   │   └── ProtectedRoute.tsx
│   │   ├── context/            # React context (Auth)
│   │   ├── services/           # API services
│   │   ├── types/              # TypeScript interfaces
│   │   └── App.tsx
│   ├── public/                 # Static assets
│   ├── package.json           # Node.js dependencies
│   ├── Dockerfile             # Frontend Docker config
│   └── nginx.conf             # Nginx configuration
├── chatbot/                   # Spring Boot backend service
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── chatbot/
│   │   │   │       ├── application/        # Use cases and services
│   │   │   │       ├── domain/             # Domain entities and repositories
│   │   │   │       ├── infrastructure/     # External implementations
│   │   │   │       ├── Interface/          # Controllers and DTOs
│   │   │   │       └── ChatbotApplication.java
│   │   │   └── resources/
│   │   │       ├── application.properties  # Configuration
│   │   │       └── db/migration/           # Database schema
│   │   └── test/                           # Unit tests
│   ├── pom.xml                            # Maven dependencies
│   ├── mvnw                               # Maven wrapper (Unix)
│   ├── mvnw.cmd                           # Maven wrapper (Windows)
│   └── Dockerfile                         # Backend Docker config
```

---

## Clean Architecture Implementation

This project is a **exemplary implementation** of Clean Architecture principles, ensuring maintainability, testability, and scalability:

### 🏛️ **Architecture Layers**

#### **1. Domain Layer** (`chatbot/domain/`)
- **Pure business logic** with no external dependencies
- **Entities**: `User`, `Conversation`, `Message` - core business objects
- **Repository Interfaces**: Abstract contracts for data access
- **Business Rules**: Conversation management, message validation
- **Value Objects**: Immutable objects representing business concepts

#### **2. Application Layer** (`chatbot/application/`)
- **Use Cases**: Single-responsibility business operations
  - `LoginUseCase` - User authentication logic
  - `CreateConversationUseCase` - Conversation creation with business rules
  - `SendMessageUseCase` - Message handling and AI context management
  - `GetHistoryUseCase` - Conversation retrieval with user validation
- **Services**: Application-specific business logic
- **DTOs**: Data transfer objects for use case inputs/outputs

#### **3. Infrastructure Layer** (`chatbot/infrastructure/`)
- **External Dependencies**: Database, AI services, security
- **Repository Implementations**: Concrete data access using Spring Data
- **AI Service Integration**: Google Gemini API client implementation
- **Security Configuration**: JWT token management and validation
- **Database Configuration**: PostgreSQL connection and schema management

#### **4. Interface Layer** (`chatbot/Interface/`)
- **Controllers**: REST API endpoints following RESTful principles
- **DTOs**: Request/Response objects for API communication
- **Exception Handlers**: Centralized error handling and response formatting
- **API Documentation**: Swagger/OpenAPI specifications

### 🔗 **Dependency Rule Compliance**

```
┌─────────────────────────────────────────────────────────────┐
│                    Interface Layer                          │
│  ┌─────────────────────────────────────────────────────┐    │
│  │              Infrastructure Layer                   │    │
│  │  ┌─────────────────────────────────────────────┐    │    │
│  │  │            Application Layer                │    │    │
│  │  │  ┌─────────────────────────────────────┐    │    │    │
│  │  │  │         Domain Layer                │    │    │    │
│  │  │  │    (No Dependencies)                │    │    │    │
│  │  │  └─────────────────────────────────────┘    │    │    │
│  │  └─────────────────────────────────────────────┘    │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

- **Inner layers never depend on outer layers**
- **Dependencies point inward** using Dependency Inversion
- **Business logic isolated** from frameworks and external concerns
- **Easy to test** each layer independently

### 🎯 **Context Management Architecture**

Our conversation context system demonstrates Clean Architecture principles:

1. **Domain**: `Conversation` entity manages context rules
2. **Application**: `SendMessageUseCase` orchestrates context preservation  
3. **Infrastructure**: Repository implementations persist conversation state
4. **Interface**: Controllers expose context-aware APIs

This ensures that **conversation context is preserved across sessions** while maintaining architectural integrity.

### Key Technologies

**Frontend:**
- **React 19** - Frontend framework
- **TypeScript** - Type-safe JavaScript
- **Material-UI (MUI)** - Component library
- **React Router** - Client-side routing
- **Axios** - HTTP client
- **Docker + Nginx** - Containerization and serving

**Backend:**
- **Spring Boot 3.5.0** - Main framework
- **Java 21** - Programming language
- **PostgreSQL** - Database
- **Google Gemini AI** - AI service for generating responses
- **JWT** - Authentication tokens
- **SpringDoc OpenAPI** - API documentation
- **Spring Security** - Security framework
- **Docker** - Containerization

---

## Running Tests

### Backend Tests
```bash
cd chatbot
./mvnw test
```

### Frontend Tests
```bash
cd frontend
npm test
```

### Run tests in Docker
```bash
# Backend tests
docker-compose exec chatbot-backend ./mvnw test

# Frontend tests  
docker-compose exec chatbot-frontend npm test
```

---

## Configuration

### Backend Configuration (`chatbot/src/main/resources/application.properties`):
- **Database**: PostgreSQL connection settings
- **Google AI API**: Google Gemini API key configuration  
- **AI Service**: Mock mode toggle for testing (set `ai.service.mock=true` for testing)

### Frontend Configuration:
- **API URL**: Automatically configured for development/production
- **Proxy**: Development proxy to backend (configured in `package.json`)
- **Nginx**: Production reverse proxy configuration (`nginx.conf`)

### Docker Configuration:
- **Networks**: All services communicate through `chatbot-network`
- **Volumes**: PostgreSQL data persistence
- **Environment Variables**: Database connection settings

---

## Contact

For issues, feel free to create an issue or pull request and contact the development team.
