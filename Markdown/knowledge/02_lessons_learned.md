# 📚 LESSONS LEARNED

## Purpose
Store important lessons, mistakes, and best practices learned during development.

AI MUST read this file before starting any task.

---

# 🧠 LESSON RULES (MANDATORY)

- Always add new lessons after fixing a bug
- Avoid repeating the same mistake
- Apply lessons to future tasks
- Update continuously

---

# ❌ COMMON MISTAKES & FIXES

## Lesson 1: Missing Input Validation
- Problem: API accepts invalid data
- Impact: System crashes or inconsistent data
- Solution: Always validate input using DTO validation
- Rule: NEVER trust user input

---

## Lesson 2: Putting Logic in Controller
- Problem: Controller becomes too complex
- Impact: Hard to maintain
- Solution: Move logic to Service layer
- Rule: Controllers must stay thin

---

## Lesson 3: Not Handling Errors Properly
- Problem: Unhandled exceptions
- Impact: API crashes
- Solution: Use global exception handler
- Rule: Always handle exceptions

---

## Lesson 4: JWT Misconfiguration
- Problem: Token not validated correctly
- Impact: Security risk
- Solution: Verify token in every request
- Rule: Secure all protected endpoints

---

## Lesson 5: Duplicate Code
- Problem: Same logic repeated
- Impact: Hard to maintain
- Solution: Refactor into reusable functions
- Rule: DRY (Don't Repeat Yourself)

---

# 🧪 TESTING LESSONS

## Lesson 6: Skipping API Testing
- Problem: Bugs go unnoticed
- Solution: Test every API immediately after writing
- Rule: NO TEST → NO PROGRESS

---

## Lesson 7: Not Testing Edge Cases
- Problem: System fails on unexpected input
- Solution: Test invalid cases
- Rule: Always test failure scenarios

---

# 🗄️ DATABASE LESSONS

## Lesson 8: Inconsistent Data Structure
- Problem: MongoDB documents inconsistent
- Solution: Define clear schema
- Rule: Keep data structure consistent

---

# ⚙️ PERFORMANCE LESSONS

## Lesson 9: Fetching Too Much Data
- Problem: Slow APIs
- Solution: Use pagination / filtering
- Rule: Only return necessary data

---

# 🚀 NEW LESSON TEMPLATE

## Lesson X: <Title>
- Problem:
- Cause:
- Fix:
- Prevention Rule:

---

# 🧠 AI INSTRUCTION

Before coding:
- Read this file
- Apply all lessons

After fixing a bug:
- Add a new lesson

NEVER repeat the same mistake twice