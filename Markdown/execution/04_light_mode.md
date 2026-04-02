# ⚡ LIGHT MODE (Context Optimization)

## Purpose
For simple tasks, reduce unnecessary reading to save time and prevent confusion.

---

## 🎯 When to Use Light Mode?

Use Light Mode if task is:
- ✅ Small (< 2 hours estimated)
- ✅ Clear (no dependencies!)
- ✅ Straightforward (no complex logic)
- ✅ Isolated (doesn't affect other systems)

**Examples of Light Mode tasks:**
- Add a simple API endpoint
- Create a simple React component
- Add validation to existing form
- Fix a small bug
- Add documentation
- Create a utility function

**NOT Light Mode tasks:**
- Major features
- Complex integrations
- System redesigns
- Multiple dependencies

---

## 📋 Light Mode Reading List

### DO READ (Minimal):
```
REQUIRED:
1. Work-item file
2. design-system/03_coding-standards.md

OPTIONAL:
3. design-system/02_architecture.md (only if confused)
```

### DO NOT READ:
```
❌ Full planning
❌ Entire project docs
❌ All knowledge base
❌ Entire work-items
❌ Historical logs
```

**Total files:** 1-2 (vs. normal 5+)
**Time saved:** 20-30 minutes per task

---

## ⚡ Light Mode Checklist

Before starting task:

```
LIGHT MODE ACTIVATION:
- [ ] Task < 2 hours?
- [ ] Task is isolated?
- [ ] No complex dependencies?
- [ ] Clear requirements?
- [ ] You know the codebase?

If ALL YES → Use Light Mode ✅
If ANY NO → Use Normal Mode ❌
```

---

## 🔥 Light Mode Workflow

### Step 1: Quick Read (5 min)
```
Read ONLY:
- work-item
- coding-standards.md
```

**Skip:**
- Planning docs
- Architecture docs
- Knowledge base
- Other work-items

### Step 2: Quick Code (1-1.5 hours)
```
Write code:
- Fast
- Direct
- Follow standards from step 1
```

### Step 3: Quick Test (15 min)
```
Test:
- Manual test
- No full test suite (unless complex)
- Verify it works
```

### Step 4: Quick Review (10 min)
```
Review:
- Read your code once
- Check for obvious issues
- Fix any problems
```

### Step 5: Quick Commit (5 min)
```
Commit:
- Short message
- Update checklist
- Push
```

**Total time:** < 2 hours

---

## 📊 Light Mode vs Normal Mode

| Aspect | Light Mode | Normal Mode |
|--------|-----------|------------|
| **Reading** | 1-2 files | 5+ files |
| **Time** | < 2 hours | 4+ hours |
| **Complexity** | Simple | Complex |
| **Dependencies** | None/Few | Many |
| **Files read** | Task + Standards | Task + all design + all state |
| **Testing** | Quick manual | Full suite |
| **Use for** | Small fixes | Major features |

---

## 🚀 Light Mode Examples

### Example 1: Add Validation

**Task:** Add email validation to register form

**Light Mode:**

1. Read: work-item (just validation part)
2. Read: coding-standards.md
3. Code: Add validation (30 min)
4. Test: Try valid + invalid emails (10 min)
5. Review: Check code (5 min)
6. Commit: Done (5 min)

**Total:** 50 minutes

---

### Example 2: Bug Fix

**Task:** Fix typo in error message

**Light Mode:**

1. Read: work-item (find bug location)
2. Code: Change text (5 min)
3. Test: Trigger error (5 min)
4. Commit: Done (2 min)

**Total:** 12 minutes

---

### Example 3: Simple Component

**Task:** Create a button component

**Light Mode:**

1. Read: coding-standards (React part)
2. Code: Component (45 min)
3. Test: Manually click button (5 min)
4. Review: Check code (5 min)
5. Commit: Done (5 min)

**Total:** 60 minutes

---

## ⚠️ Light Mode Rules

### DO:
- ✅ Use for small, clear tasks
- ✅ Follow coding standards
- ✅ Test before committing
- ✅ Update checklist
- ✅ Commit with good message

### DO NOT:
- ❌ Skip testing
- ❌ Skip code review
- ❌ Skip workflow steps
- ❌ Make assumptions
- ❌ Ignore dependencies

---

## 🔄 When to Switch Back to Normal Mode

**During Light Mode, if you discover:**

```
- [ ] Task is more complex than expected
- [ ] Dependencies found
- [ ] Unclear requirements
- [ ] Need to understand architecture
- [ ] Code doesn't work (after 1 attempt)

Action: STOP → Switch to Normal Mode
- Read all planning docs
- Read architecture
- Read all work-items (context)
- Use full workflow
- Allocate 4+ hours
```

---

## 📝 Light Mode Detection Flowchart

```
            Task given
               ↓
        Can complete in 2h?
            ↙      ↖
          YES      NO
           ↓       ↓
        Clear   Complex
       requirements? → Normal Mode
          ↙      ↖
        YES      NO
         ↓       ↓
    Isolated? Normal
      ↙    ↖    Mode
    YES   NO
     ↓    ↓
  LIGHT  Normal
  MODE   Mode
```

---

## 🧠 AI INSTRUCTION

**When task is given:**

1. Estimate time
2. Check dependencies
3. Compare with Light Mode checklist
4. Decide: Light or Normal mode

**For Light Mode:**
```
DO:
1. Ask: "Is this Light Mode task?"
2. If YES: Read only 2 files
3. Code quickly
4. Test quickly
5. Commit

DO NOT:
1. Read unnecessary files
2. Overthink simple tasks
3. Skip testing
4. Skip checklist update
```

**For Normal Mode:**
```
Follow execution/02_system_workflow.md
Full process, all files
```

---

## 🎯 Light Mode Targets

### Time Saved per Task:
- Normal: 4-6 hours
- Light: 1-2 hours
- **Savings: 50-75%**

### Context Reduced:
- Normal: Read 8-10 files
- Light: Read 2 files
- **Reduction: 75%**

### Success Rate:
- Normal: 95%
- Light: 90% (acceptable for simple tasks)

---

## Summary

Light Mode = **Fast execution for simple tasks**

Use when:
- Small task
- Clear requirements
- No complex dependencies
- You know the system

Benefits:
- ⚡ Fast (1-2 hours)
- 🎯 Focused
- 📚 Less reading
- 💪 More execution
