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