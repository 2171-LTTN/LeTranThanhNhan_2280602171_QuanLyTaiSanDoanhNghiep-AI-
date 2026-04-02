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

## Subtasks (MANDATORY - DO IN ORDER)

**Follow these steps exactly. Do NOT skip or reorder:**

1. ✅ **Create Asset Entity/Model**
   - Fields: id, name, category, status (enum), purchaseDate, assignedTo, createdAt, updatedAt
   - Add status enum: AVAILABLE, IN_USE, BROKEN
   - Add validation annotations

2. ✅ **Create AssetHistory Entity/Model**
   - Fields: id, assetId, userId, action (enum), timestamp
   - Actions: CREATED, ASSIGNED, RETURNED, UPDATED, DELETED

3. ✅ **Create AssetRepository**
   - Extend MongoRepository<Asset>
   - Add method: findByStatus(status)
   - Add method: findByAssignedTo(userId)
   - Add pagination support

4. ✅ **Create AssetHistoryRepository**
   - Extend MongoRepository<AssetHistory>
   - Add method: findByAssetId(assetId)
   - With sorting by timestamp desc

5. ✅ **Create DTOs**
   - CreateAssetRequest (name, category, purchaseDate)
   - UpdateAssetRequest (name, category)
   - AssetResponse (id, name, category, status, assignedTo, purchaseDate)
   - AssignAssetRequest (userId)
   - AssignAssetResponse (message, asset)

6. ✅ **Create AssetService - CRUD Operations**
   - createAsset(CreateAssetRequest) → save + log history
   - getAllAssets() → return paginated list
   - getAssetById(id) → return + check exists
   - updateAsset(id, UpdateAssetRequest) → update + log history
   - deleteAsset(id) → delete + log history

7. ✅ **Create AssetService - Assignment Operations**
   - assignAsset(assetId, userId) → check if available → assign → update status to IN_USE → log
   - returnAsset(assetId) → check if assigned → clear assignedTo → update status to AVAILABLE → log
   - Validation: Cannot assign if already assigned, etc.

8. ✅ **Create AssetHistoryService**
   - saveHistory(assetId, userId, action)
   - getAssetHistory(assetId) → return all history for asset

9. ✅ **Create AssetController**
   - GET /api/assets → getAllAssets()
   - GET /api/assets/{id} → getAssetById()
   - POST /api/assets → createAsset() [ADMIN only]
   - PUT /api/assets/{id} → updateAsset()
   - DELETE /api/assets/{id} → deleteAsset() [ADMIN only]
   - POST /api/assets/{id}/assign → assignAsset()
   - POST /api/assets/{id}/return → returnAsset()

10. ✅ **Add Security + Validation**
    - Protect all endpoints with @Secured/@PreAuthorize
    - Add input validation
    - Add error handling (404, 400, 403)
    - Check JWT authentication for all endpoints

11. ✅ **Write Unit Tests**
    - Test CRUD: create, read, update, delete
    - Test Assignment: assign available → OK, assign already assigned → Error
    - Test Return: return assigned → OK, return not assigned → Error
    - Test History: assignment creates history entry

12. ✅ **Test All APIs (Postman/curl)**
    - Test all 7 endpoints
    - Test with valid data → success
    - Test with invalid data → error
    - Test authentication → no token → 401
    - Test authorization → non-admin delete → 403

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