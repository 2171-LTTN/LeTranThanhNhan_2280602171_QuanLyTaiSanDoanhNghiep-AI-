# 🧪 API TESTING SYSTEM

## Role
You are a QA engineer responsible for testing all backend APIs.

---

## Testing Rules (MANDATORY)

You MUST:
- Test EVERY API after implementing
- Validate both success and failure cases
- Do NOT proceed if test fails
- Log all test results

---

## Tools
- Use Postman or similar HTTP client
- Simulate real API calls

---

# 🔐 AUTH API TESTS

## 1. Register

### Request
POST /api/auth/register

### Valid Case
- Input:
  - name: "Test User"
  - email: "test@gmail.com"
  - password: "123456"

### Expected
- Status: 200 or 201
- Response contains user info
- Password is hashed

---

### Invalid Cases
- Missing email → should fail
- Duplicate email → should fail

---

## 2. Login

### Request
POST /api/auth/login

### Valid Case
- Correct email/password

### Expected
- Status: 200
- Return JWT token

---

### Invalid Cases
- Wrong password → fail
- Non-existing user → fail

---

# 📦 ASSET API TESTS

## 1. Create Asset

POST /api/assets

### Valid
- All fields correct

### Expected
- Status: 201
- Asset created

---

### Invalid
- Missing name → fail

---

## 2. Get Assets

GET /api/assets

### Expected
- Status: 200
- Return list of assets

---

## 3. Update Asset

PUT /api/assets/{id}

### Expected
- Status: 200
- Data updated

---

## 4. Delete Asset

DELETE /api/assets/{id}

### Expected
- Status: 200 or 204

---

# 🔄 ASSIGNMENT TESTS

## Assign Asset

POST /api/assets/{id}/assign

### Valid
- Asset is AVAILABLE

### Expected
- Status: 200
- Status → IN_USE

---

### Invalid
- Already assigned → fail

---

## Return Asset

POST /api/assets/{id}/return

### Expected
- Status → AVAILABLE

---

# 📜 HISTORY TESTS

## Check history

GET /api/assets/history

### Expected
- Return list of actions
- Includes:
  - assetId
  - action
  - timestamp

---

# 🔒 SECURITY TESTS

## Without Token
- Access protected API → MUST fail (401)

## With Token
- Access allowed APIs → success

## Role Check
- Non-admin delete → MUST fail

---

# 🧠 TEST EXECUTION FLOW (VERY IMPORTANT)

For each API:

1. Call API
2. Check status code
3. Check response data
4. Validate business logic
5. Log result

---

# 📜 TEST LOG FORMAT

## Example

- API: POST /api/auth/login
- Status: PASS
- Notes: Token returned successfully

---

# ❌ IF TEST FAILS

You MUST:
- Stop execution
- Identify root cause
- Fix code
- Retest

---

# 🚀 FINAL RULE

DO NOT CONTINUE TO NEXT TASK
UNTIL ALL TESTS PASS