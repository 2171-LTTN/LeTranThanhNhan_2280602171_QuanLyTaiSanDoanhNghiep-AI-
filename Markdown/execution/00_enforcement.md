# 🚨 ENFORCEMENT RULES (MANDATORY)

## Purpose
Ensure AI NEVER skips critical steps.

---

## Rule 1: MUST complete workflow steps (NO EXCEPTION)

If you skip:
- ❌ Checklist update
- ❌ State update
- ❌ Testing
- ❌ Code review
- ❌ Activity log
- ❌ Git commit/push

Then:
🛑 **STOP IMMEDIATELY**
⚠️ Redo the entire task
❌ Do NOT proceed to next task

---

## Rule 2: Definition of Done MUST be met

Before marking task complete:
- ✅ Code runs without errors
- ✅ API/Feature works as specified
- ✅ All tests pass
- ✅ Checklist updated
- ✅ State updated
- ✅ No critical/blocking bugs
- ✅ Activity log written
- ✅ Code review passed

If ANY item fails:
🛑 **TASK INCOMPLETE** → Redo until all pass

---

## Rule 3: Priority MUST be respected

Check planning/priority.md before picking task:
- ✅ Do NOT skip P0 tasks
- ✅ Do NOT do P2 if P0/P1 not done
- ✅ Follow priority order strictly

---

## Rule 4: State MUST be updated real-time

During task:
- After starting: update state (in-progress)
- After completing: update state (completed)
- Before next task: confirm next_action

Never work without updating state

---

## Rule 5: Testing is NOT optional

For EVERY task:
- ✅ Unit tests (if applicable)
- ✅ API testing (if REST API)
- ✅ Integration testing
- ✅ Edge cases

If test fails:
🛑 Fix immediately, retest

---

## Rule 6: Code Review MUST happen

Before committing:
- ✅ Check code quality
- ✅ Check duplication
- ✅ Check security
- ✅ Check performance

If issues found:
🛑 Fix first, then commit

---

## Rule 7: Violations are BLOCKING

If ANY rule violated:
❌ Task counts as FAILED
❌ Must restart fresh
❌ No shortcuts

---

## Enforcement Checklist (Copy to each task)

Before marking DONE:

```
ENFORCEMENT CHECK:
- [ ] All workflow steps completed?
- [ ] Definition of Done met?
- [ ] Priority respected?
- [ ] State updated?
- [ ] Tests passed?
- [ ] Code reviewed?
- [ ] Activity logged?
- [ ] Ready to commit?
```

**ONLY mark [x] if ALL are true**

---

## 🎯 FINAL SELF CHECK (BEFORE MARKING DONE)

**This is the LAST step before task completion.**

Before you mark any task as DONE, answer these questions honestly:

### Code Quality
- [ ] Code compiles without errors?
- [ ] Code runs without exceptions?
- [ ] No console warnings?
- [ ] No debug logs left?
- [ ] Code is clean + readable?

### Functionality
- [ ] Feature works as specified?
- [ ] All endpoints/components work?
- [ ] All inputs handled?
- [ ] All edge cases considered?
- [ ] No missing features?

### Testing
- [ ] All tests pass (100%)?
- [ ] Unit tests written?
- [ ] Integration tests written?
- [ ] Manual testing done?
- [ ] Edge cases tested?
- [ ] Error cases tested?

### Standards Compliance
- [ ] Follows design-system rules?
- [ ] Follows coding-standards?
- [ ] Follows architecture pattern?
- [ ] Follows naming conventions?
- [ ] Follows API design?

### Completeness
- [ ] All files created?
- [ ] All methods implemented?
- [ ] No TODO/FIXME comments?
- [ ] Documentation added?
- [ ] Comments explain WHY?

### Tracking & Documentation
- [ ] Checklist updated?
- [ ] State updated?
- [ ] Activity logged?
- [ ] Bug log updated (if found)?
- [ ] Lessons learned added?

### Ready to Ship?
- [ ] Everything above checked?
- [ ] Comfortable committing?
- [ ] No hesitation?

---

**If ANY question answered NO:**
```
🛑 TASK IS NOT DONE
↓
Go back and fix
↓
Re-test everything
↓
Come back to this checklist
↓
Repeat until ALL YES
```

**Only when ALL are YES:**
```
✅ READY TO COMMIT
↓
git commit + git push
↓
Update state/01_current_state.md
↓
MOVE TO NEXT TASK
```

---

## Implementation Notes

For AI: Read this file before EVERY task.
For Human: Check violations in logs.

NO EXCEPTIONS. NO SHORTCUTS.
