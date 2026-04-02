# 🧠 CONTEXT RULES (MINIMIZE NOISE)

## Purpose
Control AI context to prevent confusion and maintain focus.

---

## 🔴 RULE 1: Read ONLY Necessary Files

**DO READ:**
- ✅ Current work-item (from work-items/)
- ✅ Design-system files (01-05)
- ✅ Current state (state/01_current_state.md)
- ✅ Enforcement rules (execution/00_enforcement.md)
- ✅ This file (execution/01_context_rules.md)

**DO NOT READ (unless blocked):**
- ❌ Other work-items
- ❌ Completed tasks
- ❌ Entire knowledge base
- ❌ All planning files
- ❌ Logs (unless debugging)

**Result:**
> Small context window = Clear thinking = Fewer mistakes

---

## 🔴 RULE 2: Context Window Priority

When reading files, priority order:

```
PRIORITY 1 (Read First):
1. Current work-item
2. Design-system/01_api-design.md
3. Design-system/03_coding-standards.md

PRIORITY 2 (Read if needed):
4. Current_state.md
5. Enforcement.md
6. Context_rules.md

PRIORITY 3 (Read only if blocked):
7. Lessons_learned.md
8. Bug_log.md
9. Architecture.md
```

**After reading Priority 1**: Start coding immediately
**Don't wait for more context**

---

## 🔴 RULE 3: File Reading Limits

| Folder | Max Files | Rule |
|--------|-----------|------|
| design-system/ | 5 | Read all (always) |
| state/ | 1 | 01_current_state.md only |
| execution/ | 2 | 00_enforcement.md + 01_context_rules.md |
| work-items/ | 1 | Current task only |
| knowledge/ | On-demand | Read if stuck 3+ times |
| planning/ | On-demand | Only if need priority |
| checklist/ | On-demand | Only to update |

---

## 🔴 RULE 4: What To Do If Lost/Confused

**If you don't know what to do:**

1. ✅ Re-read: state/01_current_state.md
2. ✅ Re-read: Current work-item
3. ✅ Check: planning/00_priority.md (next task)
4. ✅ If still lost: Read execution/recovery_mode.md

**Do NOT:**
- ❌ Read entire project
- ❌ Jump to random files
- ❌ Make assumptions

---

## 🔴 RULE 5: Context Limit per Task

| Phase | Context Limit | What to Read |
|-------|---------------|--------------|
| PREPARE | 3 files | Task + Design-system + State |
| EXECUTE | 2 files | Task + Design-system |
| VALIDATE | 2 files | Task + Review rules |
| COMPLETE | 2 files | Task + Checklist |

> Keep focused. Small context = Fast execution

---

## 🔴 RULE 6: When Context Gets Too Big

**If you've read 10+ files and confused:**

```
STOP immediately
↓
Close all files except:
- Current work-item
- Design-system/03_coding-standards.md
↓
Re-read current work-item from top
↓
Continue
```

This resets context and prevents confusion.

---

## 🧠 RATIONALE

**Why minimize context?**

1. **Clarity**: Less noise = Better focus
2. **Speed**: Fewer files = Faster reading
3. **Consistency**: One source of truth per phase
4. **Mistakes**: Too much context = Forgotten details

**Example:**
```
❌ Bad: Read design-system + planning + checklist + logs
   Result: Confused, forgotten requirements

✅ Good: Read task + design-system
   Result: Clear, focused, 1-hour completion
```

---

## 📋 AI INSTRUCTION

Before starting each task:

```
1. Read work-item [REQUIRED]
2. Read design-system 01-05 [REQUIRED]
3. Read state/01_current_state.md [REQUIRED]
4. Do NOT read anything else [HARD RULE]
5. Start coding [GO]
```

If you feel lost:
```
✅ Allowed: Re-read the 3 files above
❌ Not allowed: Read 10 other files
```

---

## Exception Cases

**When you CAN read additional files:**

1. **Blocked by unknown error** → Read knowledge/02_lessons_learned.md
2. **Need security advice** → Read design-system/02_architecture.md
3. **Test failing mysteriously** → Read checklist/logs/02_bug_log.md
4. **Task seems impossible** → Read execution/recovery_mode.md

**Otherwise**: Stick to the 3 core files.

---

## Context Checklist

Before coding, verify:

- [ ] Read current work-item
- [ ] Read design-system files (01-05)
- [ ] Read state/01_current_state.md
- [ ] Understand requirements
- [ ] Know Definition of Done
- [ ] Read ONLY these 3 + design system
- [ ] Ready to code

**If any unchecked → DO NOT START CODING**
