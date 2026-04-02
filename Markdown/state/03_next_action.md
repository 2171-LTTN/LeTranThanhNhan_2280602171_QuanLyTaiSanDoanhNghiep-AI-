# ⏭️ NEXT ACTION (CRITICAL)

## 🎯 What To Do Next

```
BEFORE you start coding:

1. Read: state/01_current_state.md
2. Read: planning/00_priority.md
3. Pick: Next task from P0/P1 list
4. Read: work-items/[module]/[task].md
5. Read: execution/00_enforcement.md
6. Start: Follow execution/02_system_workflow.md
```

---

## Next Task Template

| Field | Value |
|-------|-------|
| **Task** | [From planning/00_priority.md] |
| **Priority** | [P0/P1/P2] |
| **Location** | work-items/[module]/[task].md |
| **Est. Time** | [2-4 hours] |
| **Blockers** | [None/Check state/01_current_state.md] |

---

## Quick Start Checklist

- [ ] Read enforcement rules (execution/00_enforcement.md)
- [ ] Read priority list (planning/00_priority.md)
- [ ] Read task details (work-items/[task].md)
- [ ] Understand Definition of Done
- [ ] Follow 10-step workflow (execution/02_system_workflow.md)
- [ ] NO SKIPPING STEPS

---

## Current Recommended Next Steps

### P0: Backend Setup (MUST DO FIRST)
1. work-items/backend/01_auth.md - Authentication
2. [Create] work-items/database/01_database_setup.md - Database

### P1: Core Features (After P0)
1. work-items/backend/02_asset.md - Asset CRUD
2. work-items/frontend/01_dashboard.md - Login UI

### P2: Advanced (After P1)
1. work-items/frontend/02_asset-ui.md - Asset UI
2. Dashboard analytics

---

## Last Updated

- Date: [When last action was logged]
- Task: [What was just completed]
- Next: [What's the next task]

---

## Rules

🔴 **DO NOT:**
- Skip workflow steps
- Code without reading requirements
- Commit without testing
- Update without logging

🟢 **DO:**
- Follow priority order
- Update state after each step
- Test everything
- Ask questions if unclear

---

## Emergency Contact

**If blocked:**
1. Check state/01_current_state.md → Blocking Issues
2. Check checklist/logs/02_bug_log.md → Similar issues
3. Check knowledge/02_lessons_learned.md → Past solutions
5. Integrate with Spring Security

---

## Input Required
- User login API already exists

---

## Expected Output
- Working JWT authentication
- Secured endpoints

---

## Constraints
- Do NOT modify unrelated modules
- Follow design-system rules
- Use clean architecture

---

## After Completion
You MUST:
- Update checklist
- Update activity_log.md
- Update current_state.md
- Generate next_action.md for next task

---

## Fallback Plan
If task fails:
- Debug root cause
- Log into bug_tracker.md
- Fix and retry

---

## Status
Not Started