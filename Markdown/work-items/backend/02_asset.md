# 📦 Task: Asset Management API

## Context
Backend:
- Spring Boot
- MongoDB

Authentication already implemented.

---

## Objective
Build full CRUD + assignment system for assets.

---

## Requirements

### 1. Asset CRUD

#### Create Asset
POST /api/assets

#### Get All Assets
GET /api/assets

#### Update Asset
PUT /api/assets/{id}

#### Delete Asset
DELETE /api/assets/{id}

---

### 2. Asset Fields
- id
- name
- category
- status (AVAILABLE, IN_USE, BROKEN)
- assignedTo (userId)
- purchaseDate

---

### 3. Assignment

#### Assign Asset
POST /api/assets/{id}/assign
- Assign to user
- Update status → IN_USE

#### Return Asset
POST /api/assets/{id}/return
- Remove assigned user
- Update status → AVAILABLE

---

### 4. History Tracking
- Save all actions:
  - ASSIGNED
  - RETURNED
  - UPDATED

Collection: asset_histories

---

## Output
You MUST generate:
- Asset model
- AssetRepository
- AssetService
- AssetController
- AssetHistory model
- Assignment logic

---

## Rules
- Business logic MUST be in service layer
- Use DTOs
- Validate input
- Handle not found cases

---

## Validation
- Cannot assign if already assigned
- Cannot return if not assigned

---

## API Security
- Only authenticated users can access
- Only ADMIN can delete

---

## After Completion
- Update checklist/backend_checklist.md
- Update progress_tracking.md
- Log activity
- Update next_task.md

---

## Definition of Done (MANDATORY)

✅ Code Quality:
- [ ] Code runs without errors
- [ ] No console warnings
- [ ] Service layer has business logic (controller empty)
- [ ] DTOs used for API requests/responses
- [ ] Exception handling implemented
- [ ] Code reviewed (review/code_review.md)

✅ API Quality:
- [ ] All CRUD endpoints working
- [ ] Assignment endpoints working
- [ ] History tracking working
- [ ] Validation errors return meaningful messages
- [ ] Status codes correct
- [ ] Consistent response format

✅ Testing:
- [ ] Unit tests for service layer
- [ ] Integration tests for APIs
- [ ] All tests pass (100%)
- [ ] Happy path tested
- [ ] Error scenarios tested
- [ ] Edge cases tested (duplicate assign, invalid IDs, etc.)

✅ Compliance:
- [ ] Checklist updated (checklist/01_backend_checklist.md)
- [ ] State updated (state/01_current_state.md)
- [ ] Activity logged (checklist/logs/01_activity_log.md)
- [ ] No critical bugs
- [ ] No security issues

✅ Database:
- [ ] Collections created
- [ ] Indexes created
- [ ] Queries tested
- [ ] No N+1 query issues

**DO NOT mark task complete until ALL items checked**