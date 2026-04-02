# 🔄 RECOVERY MODE (When Things Go Wrong)

## Purpose
If a task is failing repeatedly, use this protocol to recover.

---

## 🚨 When to Activate Recovery Mode

Activate when:
- ❌ Task fails 3 times in a row
- ❌ Same error keeps happening
- ❌ Unclear what's wrong
- ❌ Need to restart and approach differently

---

## 📋 Recovery Protocol (Step by Step)

### Step 1: STOP & Analyze

```
Current situation:
- What failed? [Error message]
- How many times? [Count]
- Root cause: [Unknown/Partially known]
```

**Action:**
- Write down the error
- Check if you've seen it before (grep logs)
- Do NOT immediately retry

---

### Step 2: Break Down Task

**Original:** 1 large task
**After recovery:** 5-10 micro-tasks

**Example - Auth Task Recovery:**

❌ Originally: "Implement auth system"

✅ Recovery breakdown:
1. Create User model ONLY
2. Create UserRepository ONLY
3. Create DTOs ONLY
4. Create AuthService.register() ONLY
5. Test register API
6. Create AuthService.login() ONLY
7. Create JWT utility ONLY
8. Test login API
9. Create SecurityConfig ONLY
10. Test protected endpoint

**Each micro-task:** 15-30 minutes max

---

### Step 3: Check Knowledge Base

Before retrying, read:

```
Priority order:
1. checklist/logs/02_bug_log.md
   ↓ Search for similar errors
   
2. knowledge/02_lessons_learned.md
   ↓ Read related lessons
   
3. design-system/03_coding-standards.md
   ↓ Verify patterns
```

**Find:** Similar issue + solution already documented?
**If yes:** Apply the fix

---

### Step 4: Try Simplest Approach

**If original approach was complex:**

❌ Complex: "Implement full auth in one go"
✅ Simple: "Just test if User model can save to DB"

**New approach:**
- Remove everything extra
- Test ONE thing at a time
- Build incrementally

**Example:**
```
Try 1: Just create User entity (no service)
       ↓ Does it compile? YES
       
Try 2: Create UserRepository, test save()
       ↓ Does it work? YES
       
Try 3: Create AuthService.register()
       ↓ Does it work? ...
```

---

### Step 5: Log Failure & Prevention

When you finally solve it:

```
### Bug #X: [Error description]
- Found: [Date]
- Failed: [Count] times
- Problem: [Root cause]
- Solution: [What fixed it]
- Prevention: [How to prevent next time]
- Lesson: [What to remember]
```

**Add to:** checklist/logs/02_bug_log.md + knowledge/02_lessons_learned.md

---

## 🎯 Recovery Checklist

When activating recovery mode:

- [ ] Stop and don't panic
- [ ] Document the error
- [ ] Search logs + lessons for similar issues
- [ ] Break task into 5-10 micro-tasks
- [ ] Simplify the approach
- [ ] Test ONE micro-task at a time
- [ ] If micro-task fails:
  - [ ] Debug deeply (add logs, prints)
  - [ ] Check assumptions
  - [ ] Ask: "What did I misunderstand?"
- [ ] Each success: commit + test
- [ ] Once solved: log lesson
- [ ] Move to next task

---

## 💡 Common Recovery Patterns

### Pattern 1: Compilation Error

**Failed:** Full code doesn't compile

**Recovery:**
```
1. Comment out half the code
2. Does it compile? 
   YES → Uncomment 1/4
   NO → Comment more
3. Binary search: Find the line
4. Fix that line
5. Uncomment progressively
```

### Pattern 2: Logic Error (Code compiles but doesn't work)

**Failed:** API returns wrong data

**Recovery:**
```
1. Add console.log/print at each step
2. Trace: input → processing → output
3. Find where value becomes wrong
4. Fix that specific spot
5. Test with simple input first
6. Then test with complex input
```

### Pattern 3: Integration Error (Code works, but integration fails)

**Failed:** Frontend can't call backend API

**Recovery:**
```
1. Test backend API alone (Postman)
   → Works?
2. Test frontend call alone (mock data)
   → Works?
3. Connect them 1 piece at a time
4. Test each connection point
5. Find breaking point
6. Fix that connection
```

### Pattern 4: Dependency Error (Missing library/config)

**Failed:** "Cannot find import X"

**Recovery:**
```
1. Check pom.xml (Java) or package.json (Node)
2. Is dependency listed?
   NO → Add it
   YES → Check version
3. Clean + rebuild
4. Clear cache if needed
5. Restart IDE
6. Try again
```

---

## 🆘 If Recovery Still Fails (Last Resort)

If task fails AFTER recovery protocol:

```
Step 1: Document everything
- Error logs
- What you tried
- Lessons learned

Step 2: Start completely fresh
- Delete partial code
- Fresh file
- Copy requirements again
- Read latest lessons_learned.md
- Start from scratch slowly

Step 3: Ask for help
- Include: error logs + recovery steps + attempts
- Reference: similar resolved issues from bug_log.md
```

---

## 📊 Recovery Statistics

**Track:**
- Failures before recovery: [N]
- Time in recovery: [M minutes]
- Attempts during recovery: [K]
- Final success: [✅]
- Lesson learned: [Text]

**Better tracking = Fewer future failures**

---

## 🧠 AI INSTRUCTION

**When this file is relevant:**

1. Task fails 3+ times → READ THIS
2. Error repeats → READ THIS
3. Confused approach → READ THIS
4. Need restart → READ THIS

**Execute:** All steps in Recovery Protocol

**Goal:** Turn failure → success + learning

**Remember:**
> 3 failures + recovery = Better than 10 failures + no learning
