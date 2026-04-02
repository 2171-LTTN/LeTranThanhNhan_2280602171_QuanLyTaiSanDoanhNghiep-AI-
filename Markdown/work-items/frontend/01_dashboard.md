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

## Subtasks (MANDATORY - DO IN ORDER)

**Follow these steps exactly. Do NOT skip or reorder:**

1. ✅ **Setup React Page Component**
   - Create Dashboard.js component
   - Setup routing (Route in App.js or Router)
   - Add authentication check (redirect if not logged in)

2. ✅ **Create Statistics Cards Component**
   - Component 1: Total Assets card
   - Component 2: Assets in Use card
   - Component 3: Available Assets card
   - Component 4: Broken Assets card
   - Each card shows: Icon + Number + Label

3. ✅ **Create API Service Layer**
   - getAssets() → call GET /api/assets
   - Handle error responses
   - Return formatted data

4. ✅ **Fetch Data on Component Mount**
   - useEffect hook on Dashboard mount
   - Call getAssets()
   - Update state with statistics
   - Handle loading state
   - Handle error state

5. ✅ **Calculate Statistics**
   - totalAssets = all assets count
   - assetsInUse = count where status === 'IN_USE'
   - availableAssets = count where status === 'AVAILABLE'
   - brokenAssets = count where status === 'BROKEN'

6. ✅ **Create Recent Activity Component**
   - Display list of recent asset actions
   - Show: Asset name, Action type, Timestamp
   - Fetch from API or generate from data
   - Show empty state if no activity

7. ✅ **Add Chart Component (Optional)**
   - Use library (Chart.js, React Chart.js)
   - Pie chart: Asset status distribution
   - Or Bar chart: Assets by category

8. ✅ **Styling + Layout**
   - Use TailwindCSS classes
   - Responsive grid layout
   - Cards aligned properly
   - Colors match design-system
   - Mobile friendly

9. ✅ **Add Loading States**
   - Show spinner while fetching
   - Show loading text in cards
   - Display skeleton if needed

10. ✅ **Add Error Handling**
    - Display error message if API fails
    - Show retry button
    - Log errors to console

11. ✅ **Test Component Locally**
    - Navigate to /dashboard
    - Verify data loads
    - Check responsive design
    - Test on mobile screen
    - Verify all statistics correct

12. ✅ **Integrate with Backend**
    - Point API calls to real backend
    - Get JWT token from localStorage
    - Add Authorization header
    - Test with real API data

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