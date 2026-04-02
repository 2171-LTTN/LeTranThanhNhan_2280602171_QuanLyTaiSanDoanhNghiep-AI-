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

---

## Definition of Done (MANDATORY)

✅ Code Quality:
- [ ] Code runs without errors
- [ ] No console warnings
- [ ] Clean code principles applied
- [ ] No duplicate code
- [ ] Meaningful variable names
- [ ] Code reviewed (review/code_review.md)

✅ API Quality:
- [ ] All endpoints working
- [ ] JWT token generation works
- [ ] Token validation works
- [ ] Error responses are consistent
- [ ] Status codes correct (200, 201, 400, 401, etc.)

✅ Testing:
- [ ] Unit tests written
- [ ] Unit tests pass (100%)
- [ ] API tests pass (Postman/curl)
- [ ] Edge cases tested
- [ ] Error scenarios tested
- [ ] Security tests passed

✅ Compliance:
- [ ] Checklist updated (checklist/01_backend_checklist.md)
- [ ] State updated (state/01_current_state.md)
- [ ] Activity logged (checklist/logs/01_activity_log.md)
- [ ] No critical bugs
- [ ] No blocking issues

✅ Documentation:
- [ ] Code has comments (why, not what)
- [ ] API documented
- [ ] Database schema documented

**DO NOT mark task complete until ALL items checked**