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
| **Est. Time** | [1-2 hours MAX] |
| **Blockers** | [None/Check state/01_current_state.md] |
| **Scope** | [ONE focused objective only] |

---

## ⚠️ TASK SIZING RULES (CRITICAL)

**Task MUST be:**
- ✅ Completable in **1-2 hours MAX**
- ✅ Testable **immediately after**
- ✅ Has **ONE clear objective**
- ✅ No dependencies on unfinished tasks
- ✅ Produces **one artifact** (one component, one API, one feature)

**Task is TOO BIG if:**
- ❌ Takes 4+ hours
- ❌ Has multiple sub-features
- ❌ Depends on other unfinished tasks
- ❌ Hard to define "done"

**If task is too big → SPLIT IT:**
```
Too big: "Implement Asset Management System"
  ↓
Split into:
1. Create Asset model + repository (1h)
2. Create Asset service + CRUD (1h)
3. Create Asset controller (1h)
4. Write tests (45 min)
5. Integrate with auth (30 min)
```

**Each becomes separate task in work-items/**

---

## Quick Start Checklist

- [ ] Read enforcement rules (execution/00_enforcement.md)
- [ ] Read priority list (planning/00_priority.md)
- [ ] Read task details (work-items/[task].md)
- [ ] Understand Definition of Done
- [ ] Follow 11-step workflow (execution/02_system_workflow.md includes Phase 3.5 optimization)
- [ ] Phase 3.5: Optimize code after writing
- [ ] NO SKIPPING STEPS
- [ ] Check: Task is 1-2 hours MAX? (If not, too big!)

---

## Current Recommended Next Steps

### P0: Backend Setup (IN PROGRESS)
1. work-items/backend/01_auth.md - Authentication ✅
2. [Create] work-items/database/01_database_setup.md - Database
3. work-items/backend/02_asset.md - Asset CRUD (split into smaller tasks)

### P1: Core Features (DO AFTER P0)
1. work-items/backend/02_asset.md - Split into:
   - 02a: Asset model + repository (1h)
   - 02b: Asset service CRUD (1h) 
   - 02c: Asset controller + API (1h)
2. work-items/frontend/01_dashboard.md - Login UI

### P2: Advanced (DO AFTER P1)
1. work-items/frontend/02_asset-ui.md
2. Dashboard analytics

---

## Last Updated

- Date: [When last action was logged]
- Task: [What was just completed]
- Next: [What's the next task]
- Size: [Ensure < 2 hours estimate]

---

## Rules

🔴 **DO NOT:**
- ❌ Create tasks > 2 hours
- ❌ Combine multiple features in one task
- ❌ Skip workflow steps
- ❌ Code without reading requirements
- ❌ Commit without testing
- ❌ Update without logging
- ❌ Vague "done" criteria

🟢 **DO:**
- ✅ Keep tasks small + focused
- ✅ One objective per task
- ✅ Always testable
- ✅ Completable in one sitting
- ✅ Follow priority order
- ✅ Update state after each step
- ✅ Test everything
- ✅ Ask questions if unclear

---

## Emergency Contact

**If blocked:**
1. Check state/01_current_state.md → Blocking Issues
2. Check checklist/logs/bug_log.md → Similar issues
3. Check knowledge/lessons_learned.md → Past solutions
4. Check execution/05_recovery_mode.md → How to recover
5. Apply execution/08_apply_lessons.md → Use past lessons in code
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