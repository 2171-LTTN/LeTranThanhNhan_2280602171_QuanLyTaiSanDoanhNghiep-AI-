# 🔌 API DESIGN

## Style
- RESTful API

---

## HTTP Methods
GET → retrieve  
POST → create  
PUT → update  
DELETE → remove  

---

## Response Format (MANDATORY)

### Success
{
  "success": true,
  "data": ...
}

---

### Error
{
  "success": false,
  "message": "Error message"
}

---

## Status Codes
- 200 OK
- 201 Created
- 400 Bad Request
- 401 Unauthorized
- 403 Forbidden
- 404 Not Found

---

## Rules
- Always return consistent format
- Do NOT return raw entity directly
- Use DTOs

---

## Validation
- Validate all inputs
- Return meaningful errors