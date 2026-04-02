# 🔥 SYSTEM WORKFLOW (MANDATORY)

**BEFORE STARTING ANY TASK:**
READ: execution/00_enforcement.md + planning/00_priority.md

---

## 10-Step Workflow (NO EXCEPTIONS)

### PHASE 1: PREPARATION
1. ✅ **Read state/current_state.md** - Know current status
2. ✅ **Read planning/priority.md** - Check priorities
3. ✅ **Read design-system/** - Know standards (API, coding, architecture)
4. ✅ **Read execution/00_enforcement.md** - Know rules
5. ✅ **Pick task from planning/00_priority.md** - Get next task

### PHASE 2: EXECUTION
6. ✅ **Read work-items/[task].md** - Understand requirements
7. ✅ **Execute task** - Write code/implementation
   - Follow design-system rules
   - Apply lessons_learned
   - Keep controller thin
   - Use DTOs
   - Handle errors properly

### PHASE 3: VALIDATION
8. ✅ **Run testing** - Test everything
   - Unit tests
   - Integration tests
   - Edge cases
   - Error scenarios
   - **NO TEST FAIL = NO PROGRESS**

9. ✅ **Run review** - Code quality check
   - review/code_review.md
   - Check for duplication
   - Check security
   - Check performance
   - Apply lessons_learned fixes

### PHASE 4: COMPLETION
10. ✅ **Update tracking** - Mark completion
   - Update checklist (checklist/01_backend_checklist.md or 02_frontend_checklist.md)
   - Update state (state/01_current_state.md)
   - Update activity log (checklist/logs/01_activity_log.md)
   - Git commit + push

---

## Definition of Done Checklist (COPY TO YOUR TASK)

Before marking task COMPLETE, verify:

```
✅ ENFORCEMENT CHECK:
- [ ] Followed all 10 workflow steps?
- [ ] Definition of Done met (ALL items)?
- [ ] All tests passed?
- [ ] Code reviewed?
- [ ] Checklist updated?
- [ ] State updated?
- [ ] Activity logged?
- [ ] Ready to commit?

ONLY mark [x] if ALL are true
```

---

## Common Mistakes to AVOID

❌ **Mistake 1**: Skip testing
✅ **Fix**: Test before moving on

❌ **Mistake 2**: Put logic in controller
✅ **Fix**: Move to service layer

❌ **Mistake 3**: Forget to update state
✅ **Fix**: Update immediately

❌ **Mistake 4**: Don't handle errors
✅ **Fix**: Add try-catch + meaningful messages

❌ **Mistake 5**: Duplicate code
✅ **Fix**: Refactor into reusable function

---

## Golden Rules

🔴 **IF YOU SKIP A STEP:**
- STOP immediately
- Redo the task
- No shortcuts

🔴 **IF TEST FAILS:**
- Do NOT commit
- Fix the issue
- Retest until pass

🔴 **IF CODE REVIEW FAILS:**
- Address all issues
- Retest
- Then commit

🔴 **IF ANYTHING UNCLEAR:**
- Re-read requirements
- Ask questions in comments
- Do NOT guess

---

## 🚨 HARD RULE (ABSOLUTE - NO EXCEPTIONS)

**If ANY of these steps are skipped:**
- ❌ Testing
- ❌ Code review
- ❌ Checklist update
- ❌ State update
- ❌ Activity logging
- ❌ Git commit

**THEN IMMEDIATE ACTION:**
```
STOP immediately
↓
REVERT all changes (git reset)
↓
REDO the entire task from [Step 1]
↓
DO NOT proceed until rule is met
```

**Examples of Hard Rule Violations:**

❌ Violation 1: Code compiles → Skip testing → Commit
Result: REVERT + REDO

❌ Violation 2: Task done → Forget checklist → Continue to next task
Result: REVERT + REDO

❌ Violation 3: Code review found issues → Still commit
Result: REVERT + REDO

**CONSEQUENCE:** 
> Violating hard rules = Task counts as FAILED
> Failed task must be restarted completely

**AI INSTRUCTION:**
Before committing:
- [ ] All 10 workflow steps completed?
- [ ] Definition of Done checklist ALL checked?
- [ ] Tests 100% passing?
- [ ] Code review approved?
- [ ] Checklists updated?
- [ ] State updated?
- [ ] Activity logged?

**If ANY unchecked → DO NOT COMMIT**

---

## Quick Reference

| Step | File | Action |
|------|------|--------|
| 1 | state/01_current_state.md | Read |
| 2 | planning/00_priority.md | Read |
| 3 | design-system/ | Read |
| 4 | execution/00_enforcement.md | Read |
| 5 | work-items/[task].md | Read |
| 6-7 | Code | Write + Test |
| 8 | review/code_review.md | Check |
| 9-10 | Tracking | Update + Commit |

---

## Execution Flow Diagram

```
START
  ↓
[1-5] PREPARE: Read all docs
  ↓
[6-7] EXECUTE: Write code + test
  ↓
Test Pass? 
  NO → Fix → Retest → Go to [6-7]
  YES ↓
[8] CODE REVIEW: Check quality
  ↓
Review Pass?
  NO → Fix → Retest → Go to [8]
  YES ↓
[9-10] UPDATE: Checklist + State + Commit
  ↓
COMMIT+ PUSH TO GIT
  ↓
END: Task Complete ✅
```

---

## Notes

- Never skip steps
- Never guess
- Always reference standards
- Always test
- Always update tracking
- Always commit with meaningful message

**VIBECODER PRINCIPLE:**
> Discipline today = Full project tomorrow