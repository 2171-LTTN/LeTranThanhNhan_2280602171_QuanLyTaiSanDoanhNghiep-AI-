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

## Subtasks (MANDATORY - DO IN ORDER)

**Follow these steps exactly. Do NOT skip or reorder:**

1. ✅ **Create User Entity/Model**
   - Fields: id, name, email, password (hashed), role, createdAt
   - Add @Entity, @Document annotations
   - Add validation annotations

2. ✅ **Create UserRepository**
   - Extend MongoRepository
   - Add method: findByEmail(String email)
   - Add method: existsByEmail(String email)

3. ✅ **Create DTOs**
   - RegisterRequest (name, email, password)
   - LoginRequest (email, password)
   - LoginResponse (token, user info)
   - UserResponse (id, name, email, role)

4. ✅ **Create JwtUtil/JwtProvider class**
   - Implement generateToken(username)
   - Implement validateToken(token)
   - Implement extractUsername(token)
   - Implement isTokenExpired(token)
   - Handle JWT secret + expiration

5. ✅ **Create AuthService**
   - Implement register(RegisterRequest)
   - Implement login(LoginRequest)
   - Use BCrypt for password hashing
   - Handle email uniqueness check
   - Return proper errors

6. ✅ **Create AuthController**
   - POST /api/auth/register → call authService.register()
   - POST /api/auth/login → call authService.login()
   - Return consistent response format (success + data/message)

7. ✅ **Create SecurityConfig**
   - Configure Spring Security
   - Set /api/auth/** as public
   - Add JWT filter for protected endpoints
   - Configure CORS if needed

8. ✅ **Create JwtFilter/JwtAuthenticationFilter**
   - Intercept requests
   - Extract token from Authorization header
   - Validate token
   - Set SecurityContext

9. ✅ **Write Unit Tests**
   - Test register: success case + duplicate email
   - Test login: success case + invalid credentials
   - Test JWT: generate + validate + extract

10. ✅ **Test All APIs (Postman/curl)**
    - POST /api/auth/register → should work
    - POST /api/auth/login → should return token
    - Test protected endpoint with token → should work
    - Test protected endpoint without token → should return 401

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