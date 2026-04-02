# 🧩 Task: Asset Management UI

## Context
You are building the frontend for an Enterprise Asset Management System.

Frontend stack:
- ReactJS
- TailwindCSS

Backend APIs already exist for asset management.

---

## Objective
Build a complete UI for managing assets.

---

## Requirements

### 1. Asset List Page
- Display all assets in a table
- Columns:
  - Name
  - Category
  - Status
  - Assigned User
  - Actions (Edit/Delete)

### 2. Add Asset
- Form fields:
  - Name
  - Category
  - Purchase Date
- Validate required fields

### 3. Edit Asset
- Load existing data into form
- Allow updating asset info

### 4. Delete Asset
- Confirm before deleting

---

## API Integration
- GET /api/assets
- POST /api/assets
- PUT /api/assets/{id}
- DELETE /api/assets/{id}

---

## Subtasks (MANDATORY - DO IN ORDER)

**Follow these steps exactly. Do NOT skip or reorder:**

1. ✅ **Create Main AssetManagement Component**
   - Create Assets.js component
   - Setup routing: /assets page
   - Add authentication check (redirect if not logged in)
   - Initialize state: assetList, loading, error

2. ✅ **Create Asset List/Table Component**
   - Display all assets in table
   - Columns: ID, Name, Category, Status, Assigned User, Actions
   - Fetch data on mount using GET /api/assets
   - Handle loading + error states
   - Add empty state if no assets

3. ✅ **Create Add Asset Form Component**
   - Modal or separate page
   - Form fields: Name, Category, Purchase Date
   - Submit button → POST /api/assets
   - Validation: required fields
   - Success → refresh table
   - Error → show error message

4. ✅ **Create Edit Asset Form Component**
   - Modal or inline edit
   - Pre-fill form with existing data
   - Fields: Name, Category
   - Submit button → PUT /api/assets/{id}
   - Validation: required fields
   - Success → refresh table
   - Error → show error message

5. ✅ **Create Delete Confirmation Dialog**
   - Modal with confirmation
   - Show: "Are you sure?"
   - Cancel button
   - Delete button → DELETE /api/assets/{id}
   - Success → remove from table
   - Error → show error message

6. ✅ **Create Action Buttons**
   - Edit button → Open edit form
   - Delete button → Show confirmation
   - In each table row

7. ✅ **Setup API Service Layer**
   - getAllAssets() → GET /api/assets
   - createAsset(data) → POST /api/assets
   - updateAsset(id, data) → PUT /api/assets/{id}
   - deleteAsset(id) → DELETE /api/assets/{id}
   - All with error handling

8. ✅ **Add Form Validation**
   - Name: required, min length 3
   - Category: required, select from dropdown
   - Purchase Date: valid date format
   - Show validation errors

9. ✅ **Add Loading/Error States**
   - Loading spinner while fetching
   - Error message display
   - Retry button on error
   - Disable buttons during loading

10. ✅ **Styling + Layout**
    - Use TailwindCSS
    - Responsive table design
    - Modal popup style
    - Form styling
    - Button styling
    - Mobile friendly

11. ✅ **Test All CRUD Operations**
    - Create new asset → ✓ appears in table
    - Edit asset → ✓ data updates
    - Delete asset → ✓ removed from table
    - Refresh page → ✓ data persists
    - Error cases → ✓ error displayed

12. ✅ **Integrate with Real Backend**
    - Connect to real API endpoints
    - Add JWT token to headers
    - Test with real data
    - Verify all operations working

---

## Definition of Done (MANDATORY)

✅ UI/UX Quality:
- [ ] All pages render without errors
- [ ] Layout responsive (mobile, tablet, desktop)
- [ ] Table displays all assets correctly
- [ ] Add/Edit forms are functional
- [ ] UI matches design specifications
- [ ] Form validation messages show correctly
- [ ] Buttons are clickable and responsive

✅ Functionality:
- [ ] List page shows all assets
- [ ] Add asset creates new record
- [ ] Edit asset updates existing record
- [ ] Delete asset removes record
- [ ] Confirm dialogs work
- [ ] API calls successful
- [ ] Data persists after page reload

✅ Error Handling:
- [ ] Network errors handled
- [ ] Validation errors shown
- [ ] 404 errors handled
- [ ] Permission errors handled
- [ ] User gets clear error messages

✅ Testing:
- [ ] Component tests pass
- [ ] All CRUD operations tested
- [ ] Form validation works
- [ ] No console errors
- [ ] API integration verified
- [ ] Manual testing on multiple browsers

✅ Compliance:
- [ ] Checklist updated (checklist/02_frontend_checklist.md)
- [ ] State updated (state/01_current_state.md)
- [ ] Activity logged (checklist/logs/01_activity_log.md)
- [ ] Code reviewed

✅ Code Quality:
- [ ] Clean, readable React code
- [ ] Components properly organized
- [ ] No duplicate code
- [ ] Proper error handling
- [ ] Comments where needed

**DO NOT mark task complete until ALL items checked**

---

## UI Requirements
- Clean UI using TailwindCSS
- Responsive design
- Use reusable components

---

## Output
You MUST generate:
- React components
- API service (axios)
- Form handling logic
- State management (useState/useEffect)

---

## Rules
- Do NOT modify unrelated files
- Keep code modular
- Explain before coding

---

## After Completion
- Update checklist/frontend_checklist.md
- Mark completed tasks
- Update progress_tracking.md
- Log activity

---

## Definition of Done
- Code runs
- API works
- Tests pass
- Checklist updated
- No critical bugs