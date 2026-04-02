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