# 🗄️ DATABASE DESIGN (MongoDB)

## Rule
- Schema must be consistent
- Avoid random fields

---

## Collections

### users
- id
- name
- email (unique)
- password (hashed)
- role

---

### assets
- id
- name
- category
- status (AVAILABLE, IN_USE, BROKEN)
- assignedTo (userId)
- purchaseDate

---

### asset_histories
- id
- assetId
- userId
- action
- timestamp

---

## Relationships
- assets.assignedTo → users.id
- asset_histories.assetId → assets.id

---

## Rules
- No duplicate data
- Validate required fields
- Keep structure stable

---

## Performance
- Index:
  - email (users)
  - assetId (history)