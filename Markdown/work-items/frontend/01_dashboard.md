# 📊 Task: Dashboard UI

## Context
This is the main dashboard of the system.

Frontend:
- ReactJS
- TailwindCSS

---

## Objective
Build a dashboard page that shows system overview.

---

## Requirements

### 1. Statistics Cards
Display:
- Total assets
- Assets in use
- Available assets
- Broken assets

---

### 2. Recent Activity
- Show recent asset history
- Include:
  - Asset name
  - Action (assigned, returned)
  - Time

---

### 3. Charts (Optional but recommended)
- Asset status distribution
- Use simple chart library (e.g., chart.js)

---

## API Integration
- GET /api/assets
- GET /api/assets/history

---

## UI Requirements
- Modern dashboard layout
- Responsive design
- Color scheme consistent with design-system

---

## Definition of Done (MANDATORY)

✅ UI/UX Quality:
- [ ] Dashboard renders without errors
- [ ] Layout responsive (mobile, tablet, desktop)
- [ ] All statistics cards display correctly
- [ ] Recent activity list shows data
- [ ] Charts render (if implemented)
- [ ] UI matches design specifications
- [ ] No broken links or missing images

✅ Functionality:
- [ ] API calls are made (GET /api/assets, etc.)
- [ ] Data refreshes correctly
- [ ] Loading states shown
- [ ] Error states handled
- [ ] Empty states handled

✅ Testing:
- [ ] Component tests pass
- [ ] API integration works
- [ ] Manual testing on multiple browsers
- [ ] No console errors
- [ ] No performance issues (no lag)

✅ Compliance:
- [ ] Checklist updated (checklist/02_frontend_checklist.md)
- [ ] State updated (state/01_current_state.md)
- [ ] Activity logged (checklist/logs/01_activity_log.md)
- [ ] Code reviewed

✅ Code Quality:
- [ ] Clean, readable code
- [ ] No duplicate code
- [ ] Proper error handling
- [ ] Comments where needed

**DO NOT mark task complete until ALL items checked**
- Cards + table layout
- Responsive

---

## Output
You MUST generate:
- Dashboard page component
- Reusable card component
- API calls
- Data mapping logic

---

## Rules
- Keep components reusable
- Optimize performance
- Do not overcomplicate UI

---

## After Completion
- Update checklist/frontend_checklist.md
- Update progress_tracking.md
- Write activity log

---

## Definition of Done
- Code runs
- API works
- Tests pass
- Checklist updated
- No critical bugs