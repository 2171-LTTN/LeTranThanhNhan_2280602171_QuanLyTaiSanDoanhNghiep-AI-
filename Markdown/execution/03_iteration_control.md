# ⏱️ ITERATION CONTROL (Loop Prevention)

## Purpose
Prevent infinite loops when AI fails repeatedly on same task.

---

## 🚨 Problem It Solves

Without iteration control:
```
Attempt 1: Code fails
  ↓ Fix
Attempt 2: Same error
  ↓ Fix differently
Attempt 3: Different error
  ↓ Fix differently
Attempt 4: Back to attempt 1 error
  ↓ INFINITE LOOP ❌
```

**Result:** Stuck for hours, no progress.

**Solution:** Iteration Control enforces breaking after N attempts.

---

## 📋 ITERATION RULE (MANDATORY)

### Max Attempts Per Task: 3

**Attempt = 1 test + 1 fix cycle**

```
Attempt 1:
- Code → Test → [FAIL]
- Fix: Try approach A
- Code → Test → [FAIL again]
↓

Attempt 2:
- Reset
- Fix: Try approach B (different)
- Code → Test → [FAIL still]
↓

Attempt 3:
- Reset
- Fix: Try approach C (completely different)
- Code → Test → [FAIL again]
↓

if still failing after 3:
🛑 STOP
→ Break task smaller
→ Use recovery_mode.md
→ Switch strategy
```

---

## 🎯 What Counts as "Attempt"

### ✅ One Attempt:
- Write code
- Run test
- See result (pass or fail)
- Make fix

### ✅ Another Attempt:
- Rewrite code significantly
- Run test
- See result
- See if it works

### ❌ NOT an attempt:
- Small tweaks/refactors (same code, same logic)
- Re-running same code (doesn't count as new attempt)
- Just formatting (no logic change)

---

## 🔄 Attempt Lifecycle

### Attempt N: [Description]

```
Start: [Time]
Approach: [What strategy trying]
Code changes: [What changed]
Test result: [Pass/Fail + error]
Analysis: [Why failed?]
Next attempt plan: [How to fix]
End: [Time]
Duration: [Minutes]
```

---

## 📊 Iteration Counter Template

**Use this for ANY failing task:**

```
ITERATION LOG:

Task: [Task name]
Total attempts: [N]
Max allowed: 3

Attempt 1:
- Approach: [Strategy]
- Result: [FAIL - Error]
- Time: [30 mins]

Attempt 2:
- Approach: [Different strategy]
- Result: [FAIL - Different error]
- Time: [45 mins]

Attempt 3:
- Approach: [Third strategy]
- Result: [FAIL - Still failing]
- Time: [40 mins]

Total time: [115 mins]
Status: [Failed after 3 attempts]

ACTION TAKEN:
→ Broke task into micro-tasks
→ Used recovery_mode.md
→ Switched to simpler approach
```

---

## 🛑 When to Trigger "Break Task Smaller"

After 3 failed attempts, you MUST:

1. **Stop current task completely**
2. **Break it into 5-10 micro-tasks**
3. **Use recovery_mode.md protocol**

**Example - Auth Task Fails 3x:**

Original task: "Implement Auth"

After 3 failures, break into:
1. Just create User entity (1 hour)
2. Just create UserRepository (30 min)
3. Just create DTOs (30 min)
4. Just save user to DB (30 min)
5. Just create AuthService.register (1 hour)
6. Just test register API (30 min)
... and so on

**Then:** Try micro-task 1 instead

---

## 📈 Iteration Tracking

### For AI:
```
Before each attempt:
- Count current attempt number
- Compare with max (3)
- If attempt >= 3: Stop and break task

After each attempt:
- Log to checklist/logs/02_bug_log.md
- Update iteration counter
- Decide: continue or break
```

### For Human:
Monitor via:
- checklist/logs/02_bug_log.md
- See "attempt count"
- If = 3: Task was appropriately abandoned

---

## 🧠 Smart Iteration

### ✅ Good Iteration
```
Attempt 1: Try approach A
  → Fails → Analyze error
  
Attempt 2: Approach A didn't work
  → Try completely different approach B
  → Different angle, different strategy
  
Attempt 3: Approach B didn't work
  → Try approach C (third completely different)
  
Result: After 3 real attempts, break task
```

### ❌ Bad Iteration
```
Attempt 1: Reindent code, test
Attempt 2: Add log statements, test
Attempt 3: Rename variables, test

❌ NOT different approaches → All same strategy
```

---

## 📝 Iteration Decision Tree

```
                    Task fails
                       ↓
              Attempt count = ?
              ↙        ↓        ↖
            1          2           3
            ↓          ↓           ↓
         Try fix     Try fix    Check: Are you
         return     differently  trying REAL
         to code      return     different
                      to code    approaches?
                                    ↙    ↖
                                  YES   NO
                                   ↓     ↓
                            Continue  Count as
                            trying    less than 3
                                      Try again
                                      Different
                                      approach
              
              After attempt 3:
                ↓
          Still failing?
              ↙      ↖
            YES       NO
             ↓        ↓
        STOP    ✅ SUCCESS
        Break   Commit
        task
```

---

## 🎓 Best Practices

### DON'T:
- ❌ Try same approach 5 times
- ❌ Just add/remove lines without thinking
- ❌ Hope it fixes itself
- ❌ Ignore iteration limit

### DO:
- ✅ Think about WHY it failed
- ✅ Plan different approach
- ✅ Document each attempt
- ✅ Break after 3 if still failing

---

## ⚠️ Common Iteration Mistakes

### Mistake 1: Same Fix, Many Times
```
❌ Add log → Test → Same error
❌ Remove log → Test → Same error
❌ Add different log → Test → Same error

This is NOT 3 attempts, it's looping
```

### Mistake 2: Minor Changes
```
❌ Attempt 1: Add semicolon
❌ Attempt 2: Change variable name
❌ Attempt 3: Reformat code

NOT different approaches
```

### Mistake 3: Ignoring Iteration Limit
```
❌ Attempt 4: Still trying
❌ Attempt 5: Still trying
❌ Hour 2: Still trying

Should have stopped after 3
```

---

## Integration with Other Files

**When iteration fails:**
1. Log to: checklist/logs/02_bug_log.md
2. Use: execution/05_recovery_mode.md
3. Update: knowledge/02_lessons_learned.md
4. Break task: planning/00_priority.md (sub-tasks)

---

## 🔔 AI INSTRUCTION

**Before EVERY fix attempt:**
- Count attempts (check bug_log)
- If attempt >= 3: STOP immediately
- Use recovery_mode.md instead
- DO NOT continue guessing

**After 3 failed attempts:**
```
1. Stop current approach
2. Log what happened
3. Read recovery_mode.md
4. Break task into smaller parts
5. Start with micro-tasks instead
```

**NEVER attempt 4+**
