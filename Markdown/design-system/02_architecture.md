# 🏗️ SYSTEM ARCHITECTURE

## Role
You are a senior Java architect.

---

## Architecture Pattern
- MVC (Model - Controller - Service - Repository)

---

## Data Flow
Client → Controller → Service → Repository → MongoDB

---

## Layer Responsibilities

### Controller
- Handle HTTP request/response
- Validate request (basic)
- Call service layer
- NO business logic

---

### Service
- Core business logic
- Data processing
- Validation logic

---

### Repository
- Data access layer
- MongoDB interaction

---

## Design Principles
- Separation of concerns
- Single Responsibility Principle
- Loose coupling
- High cohesion

---

## Rules (MANDATORY)
- NEVER put business logic in controller
- ALWAYS use service layer
- KEEP layers independent