# 🧠 PROJECT MEMORY

## Project Overview
Enterprise Asset Management System
- Backend: Spring Boot
- Database: MongoDB
- Frontend: React

---

## Completed Modules
- None

---

## In Progress
- Authentication system

---

## Pending Modules
- Asset management
- Assignment system
- Dashboard
- Deployment

---

## Key Decisions

### Decision 1
- Use MongoDB instead of SQL
- Reason: flexible schema

### Decision 2
- Use JWT authentication
- Reason: stateless & scalable

---

## Known Issues
- None

---

## Lessons Learned
- Always validate input before processing
- Keep controller thin

---

## Important Notes
- All APIs must be secured with JWT
- Use DTO instead of returning entity directly

---

## Dependencies Between Modules
- Auth must be completed before Asset module
- Asset module required for Dashboard

---

## Risks
- JWT misconfiguration may break all APIs
- MongoDB schema inconsistency

---

## Update Rules
After EACH major task:
- Update Completed Modules
- Update In Progress
- Add new decisions if needed
- Add issues if found