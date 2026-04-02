# 🔥 TASK PRIORITY SYSTEM

## Purpose
Define clear priority to avoid confusion about what to do first.

---

## 🔴 P0 - CRITICAL (MUST DO FIRST)

Scale: 1-5 (1 = must start now)

### 1. Backend Setup & Auth
- Task: work-items/backend/01_auth.md
- Impact: Blocks everything else
- Effort: High
- Must complete BEFORE: All other backend tasks
- Status: [ ] Not Started

**Subtasks:**
- [ ] Spring Boot project setup
- [ ] MongoDB config
- [ ] User model + repository
- [ ] Auth service (register/login)
- [ ] JWT implementation
- [ ] Security config
- [ ] Tests passing

---

### 2. Database Setup
- Task: (Need to create work-items/database/01_database_setup.md)
- Impact: Blocks backend data operations
- Effort: Medium
- Must complete BEFORE: Asset management
- Status: [ ] Not Started

**Subtasks:**
- [ ] MongoDB connection
- [ ] Collections created (users, assets, asset_histories)
- [ ] Indexes created
- [ ] Schema validation

---

## 🟠 P1 - HIGH (DO AFTER P0)

### 3. Backend Asset Management CRUD
- Task: work-items/backend/02_asset.md
- Impact: Core functionality
- Effort: High
- Must complete BEFORE: Dashboard
- Depends on: P0 (Auth)
- Status: [ ] Not Started

**Subtasks:**
- [ ] Asset model
- [ ] Asset repository
- [ ] Asset service (CRUD)
- [ ] Asset controller
- [ ] Tests passing

---

### 4. Frontend Setup & Login UI
- Task: work-items/frontend/01_dashboard.md (rename to 01_login.md)
- Impact: User interface entry point
- Effort: Medium
- Must complete BEFORE: Asset UI
- Depends on: P0 (Auth API)
- Status: [ ] Not Started

**Subtasks:**
- [ ] React project setup
- [ ] Login form UI
- [ ] API integration
- [ ] JWT storage (localStorage)
- [ ] Tests passing

---

## 🟡 P2 - MEDIUM (DO AFTER P1)

### 5. Asset History Tracking
- Task: (Need to create)
- Impact: Nice to have, not critical
- Effort: Low
- Depends on: P0 + P1
- Status: [ ] Not Started

---

### 6. Frontend Asset UI
- Task: work-items/frontend/02_asset-ui.md
- Impact: Main user feature
- Effort: High
- Depends on: P1 (Login) + P1 (Asset CRUD)
- Status: [ ] Not Started

**Subtasks:**
- [ ] Asset list view
- [ ] Create/Edit/Delete UI
- [ ] API integration
- [ ] Tests passing

---

### 7. Dashboard
- Task: work-items/frontend/01_dashboard.md (move to P2)
- Impact: Analytics/overview
- Effort: Medium
- Depends on: All P1 done
- Status: [ ] Not Started

---

## 🟢 P3 - LOW (NICE TO HAVE)

### 8. Advanced Features
- Asset assignment UI
- History timeline UI
- Performance optimization
- UI theme customization

---

## 📊 Priority Matrix

```
MUST DO FIRST          AFTER              AFTER THAT
─────────────────      ─────────────────  ─────────────
P0: Auth               P1: Asset CRUD     P2: Dashboard
P0: Database           P1: Login UI       P2: Asset UI
                       
Est. Time:             Est. Time:         Est. Time:
2-3 days               3-4 days           2 days
```

---

## 🧠 Rules

1. **Do NOT skip P0** - Everything depends on it
2. **Do NOT do P2 before P1 is done** - Waste of effort
3. **P1 items can be parallel** - Backend + Frontend can work together
4. **Priority updates** - If blocker found, escalate to P0
5. **Current week focus** - Pick 1-2 P1 items MAX per week

---

## Current Status

|Priority|Task|Status|ETA|
|--------|----|----|---|
|P0|Backend Setup|❌|Day 1-2|
|P0|Database Setup|❌|Day 1|
|P1|Asset CRUD|❌|Day 3-4|
|P1|Login UI|❌|Day 2-3|
|P2|Asset UI|❌|Day 5-6|
|P3|Dashboard|❌|Later|

---

## Decision Logic

**If confused about what to do:**
1. Check current_state.md → see what's in progress
2. Check this file → what's next in priority
3. Read work-items/[current]/[task].md → get details
4. Start coding

NO GUESSING. USE THIS FILE.
