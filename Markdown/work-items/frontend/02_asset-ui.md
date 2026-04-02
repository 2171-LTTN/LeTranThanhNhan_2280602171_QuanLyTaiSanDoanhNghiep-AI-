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