# 🔐 Task: Authentication System (Spring Boot + JWT)

## Context
Backend is built using:
- Java 17
- Spring Boot
- MongoDB
- Spring Security

---

## Objective
Implement a complete authentication system using JWT.

---

## Requirements

### 1. Register API
- Endpoint: POST /api/auth/register
- Fields:
  - name
  - email
  - password
- Validate input
- Hash password before saving

---

### 2. Login API
- Endpoint: POST /api/auth/login
- Validate email + password
- Generate JWT token

---

### 3. JWT Security
- Create JWT utility class
- Generate token
- Validate token
- Extract user info from token

---

### 4. Security Config
- Configure Spring Security
- Protect APIs
- Allow public:
  - /api/auth/**

---

## Database
Collection: users
- id
- name
- email
- password
- role

---

## Output
You MUST generate:
- User model (Entity)
- UserRepository (MongoRepository)
- AuthService
- AuthController
- JWT Utility
- SecurityConfig
- DTOs (request/response)

---

## Rules
- Use clean architecture (Controller → Service → Repository)
- Do NOT put logic in controller
- Use DTO instead of entity directly
- Handle exceptions properly

---

## Validation
- Email must be unique
- Password must be hashed (BCrypt)

---

## After Completion
- Update checklist/backend_checklist.md
- Mark completed tasks [x]
- Update progress_tracking.md
- Write activity log
- Update state/current_state.md