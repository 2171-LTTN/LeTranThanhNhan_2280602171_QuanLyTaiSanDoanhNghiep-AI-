# 🐛 BUG LOG

## Purpose
Track all bugs found during development for learning and prevention.

---

## Template

```
### Bug #[N]: [Bug Title]
- Found: [Date]
- Module: Backend/Frontend
- Severity: Critical/High/Medium/Low
- Description: [What went wrong]
- Cause: [Root cause analysis]
- Fix: [Solution applied]
- Test: [How verified]
- Prevention: [How to prevent this in future]
- Status: ✅ Fixed / 🔄 In Progress / ⏳ Pending
```

---

## Bug Records

### TEMPLATE ENTRY (Copy and fill below)

### Bug #1: JWT Token Not Validating
- Found: 2026-04-02
- Module: Backend
- Severity: Critical
- Description: JWT token validation fails on protected endpoints
- Cause: Token expiration check was skipped in SecurityConfig
- Fix: Added expiration validation to JWT utility
- Test: API now returns 401 for expired tokens
- Prevention: Add test for token expiration + review SecurityConfig rules
- Status: ✅ Fixed

---

## Active Bugs

- None currently

---

## Instructions

1. **When finding a bug:**
   - Add to this log immediately
   - Include root cause analysis
   - Links to lessons_learned.md if applicable

2. **After fixing:**
   - Mark as ✅ Fixed
   - Update prevention rule
   - Add to lessons_learned.md

3. **Severity levels:**
   - 🔴 Critical: Blocks feature, security risk
   - 🟠 High: Major functionality broken
   - 🟡 Medium: Feature works but suboptimal
   - 🟢 Low: Minor UI/UX issue

4. **Prevention:** Use to update lessons_learned.md