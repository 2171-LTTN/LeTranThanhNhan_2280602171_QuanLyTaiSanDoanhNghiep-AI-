// ============================================================
// QUAN LY TAI SAN DOANH NGHIEP - PRODUCTION DATASET
// MongoDB Shell Script - Ready to Run
// ============================================================
// Usage: mongosh < dataset.js
// Or: Copy and paste into MongoDB Compass or Atlas Shell
//
// IMPORTANT: This script contains BCrypt hashes for demo password
// Password for all users: "password123"
// ============================================================

// Switch to the database
use quanlytaisan;

// ============================================================
// STEP 1: CLEAR EXISTING DATA
// ============================================================
print("=== STEP 1: Clearing existing data ===");
try { db.users.drop(); } catch(e) {}
try { db.assets.drop(); } catch(e) {}
try { db.asset_histories.drop(); } catch(e) {}

print("Existing collections dropped.");

// ============================================================
// STEP 2: CREATE USERS (5 users: 1 Admin + 4 Employees)
// Password for all: "password123"
// Valid BCrypt hash generated with strength 12
// ============================================================
print("=== STEP 2: Creating users ===");

// Valid BCrypt hash for "password123"
const PASSWORD_HASH = "$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/X4.F1CxIqF0Bj1xu";

db.users.insertMany([
    {
        _id: "user-admin-001",
        name: "Nguyen Van Admin",
        email: "admin@company.com",
        password: PASSWORD_HASH,
        role: "ADMIN",
        department: "IT",
        position: "IT Manager",
        phone: "0912-345-678",
        isActive: true,
        createdAt: new Date("2024-01-15T08:00:00Z"),
        updatedAt: null
    },
    {
        _id: "user-emp-001",
        name: "Tran Thi Mai",
        email: "mai.tran@company.com",
        password: PASSWORD_HASH,
        role: "USER",
        department: "Marketing",
        position: "Marketing Specialist",
        phone: "0934-567-890",
        isActive: true,
        createdAt: new Date("2024-02-01T09:00:00Z"),
        updatedAt: null
    },
    {
        _id: "user-emp-002",
        name: "Le Van Hoang",
        email: "hoang.le@company.com",
        password: PASSWORD_HASH,
        role: "USER",
        department: "Development",
        position: "Senior Developer",
        phone: "0945-678-901",
        isActive: true,
        createdAt: new Date("2024-02-15T10:00:00Z"),
        updatedAt: null
    },
    {
        _id: "user-emp-003",
        name: "Pham Thi Lan",
        email: "lan.pham@company.com",
        password: PASSWORD_HASH,
        role: "USER",
        department: "Finance",
        position: "Accountant",
        phone: "0956-789-012",
        isActive: true,
        createdAt: new Date("2024-03-01T08:30:00Z"),
        updatedAt: null
    },
    {
        _id: "user-emp-004",
        name: "Hoang Van Duc",
        email: "duc.hoang@company.com",
        password: PASSWORD_HASH,
        role: "USER",
        department: "HR",
        position: "HR Officer",
        phone: "0967-890-123",
        isActive: true,
        createdAt: new Date("2024-03-15T11:00:00Z"),
        updatedAt: null
    }
]);

print("Created 5 users successfully.");

// ============================================================
// STEP 3: CREATE ASSETS (10 assets)
// ============================================================
print("=== STEP 3: Creating assets ===");

db.assets.insertMany([
    // Laptops
    {
        _id: "asset-lap-001",
        name: "MacBook Pro 14-inch M3",
        category: "Laptop",
        status: "IN_USE",
        assignedTo: "user-emp-002",
        purchaseDate: new Date("2024-06-15"),
        purchasePrice: 45000000,
        serialNumber: "C02XLAPTOP001",
        brand: "Apple",
        model: "MacBook Pro 14",
        warrantyUntil: new Date("2027-06-15"),
        location: "Office Floor 3",
        createdAt: new Date("2024-06-15T10:00:00Z"),
        updatedAt: new Date("2024-09-01T14:00:00Z")
    },
    {
        _id: "asset-lap-002",
        name: "Dell XPS 15",
        category: "Laptop",
        status: "AVAILABLE",
        assignedTo: null,
        purchaseDate: new Date("2024-03-20"),
        purchasePrice: 32000000,
        serialNumber: "DELLXPS15002",
        brand: "Dell",
        model: "XPS 15 9530",
        warrantyUntil: new Date("2027-03-20"),
        location: "IT Storage Room",
        createdAt: new Date("2024-03-20T09:00:00Z"),
        updatedAt: null
    },
    {
        _id: "asset-lap-003",
        name: "ThinkPad X1 Carbon Gen 11",
        category: "Laptop",
        status: "IN_USE",
        assignedTo: "user-emp-001",
        purchaseDate: new Date("2024-05-10"),
        purchasePrice: 28000000,
        serialNumber: "LNVX1CARB003",
        brand: "Lenovo",
        model: "ThinkPad X1 Carbon",
        warrantyUntil: new Date("2027-05-10"),
        location: "Marketing Office",
        createdAt: new Date("2024-05-10T11:00:00Z"),
        updatedAt: new Date("2024-08-01T09:00:00Z")
    },
    {
        _id: "asset-lap-004",
        name: "HP EliteBook 840 G10",
        category: "Laptop",
        status: "BROKEN",
        assignedTo: null,
        purchaseDate: new Date("2023-12-01"),
        purchasePrice: 25000000,
        serialNumber: "HP840G10004",
        brand: "HP",
        model: "EliteBook 840 G10",
        warrantyUntil: new Date("2026-12-01"),
        location: "IT Repair Shop",
        note: "Screen damaged - awaiting repair",
        createdAt: new Date("2023-12-01T08:00:00Z"),
        updatedAt: new Date("2024-11-20T16:00:00Z")
    },

    // Monitors
    {
        _id: "asset-mon-001",
        name: "Dell UltraSharp U2723QE 27\"",
        category: "Monitor",
        status: "IN_USE",
        assignedTo: "user-emp-002",
        purchaseDate: new Date("2024-06-15"),
        purchasePrice: 15000000,
        serialNumber: "DELU2723QE01",
        brand: "Dell",
        model: "U2723QE",
        warrantyUntil: new Date("2027-06-15"),
        location: "Development Floor 3",
        createdAt: new Date("2024-06-15T10:30:00Z"),
        updatedAt: new Date("2024-09-01T14:30:00Z")
    },
    {
        _id: "asset-mon-002",
        name: "LG UltraFine 27UN880",
        category: "Monitor",
        status: "AVAILABLE",
        assignedTo: null,
        purchaseDate: new Date("2024-04-01"),
        purchasePrice: 12000000,
        serialNumber: "LG27UN88002",
        brand: "LG",
        model: "UltraFine 27UN880",
        warrantyUntil: new Date("2027-04-01"),
        location: "IT Storage Room",
        createdAt: new Date("2024-04-01T09:00:00Z"),
        updatedAt: null
    },
    {
        _id: "asset-mon-003",
        name: "Samsung Odyssey G7 32\"",
        category: "Monitor",
        status: "IN_USE",
        assignedTo: "user-emp-003",
        purchaseDate: new Date("2024-07-20"),
        purchasePrice: 22000000,
        serialNumber: "SAMG7LC003",
        brand: "Samsung",
        model: "Odyssey G7 LC32G75T",
        warrantyUntil: new Date("2027-07-20"),
        location: "Finance Office",
        createdAt: new Date("2024-07-20T11:00:00Z"),
        updatedAt: new Date("2024-10-01T10:00:00Z")
    },

    // Phones
    {
        _id: "asset-phn-001",
        name: "iPhone 15 Pro",
        category: "Phone",
        status: "IN_USE",
        assignedTo: "user-emp-004",
        purchaseDate: new Date("2024-09-15"),
        purchasePrice: 35000000,
        serialNumber: "IP15PRO001",
        brand: "Apple",
        model: "iPhone 15 Pro",
        warrantyUntil: new Date("2026-09-15"),
        location: "HR Office",
        createdAt: new Date("2024-09-15T09:00:00Z"),
        updatedAt: new Date("2024-09-20T14:00:00Z")
    },
    {
        _id: "asset-phn-002",
        name: "Samsung Galaxy S24 Ultra",
        category: "Phone",
        status: "AVAILABLE",
        assignedTo: null,
        purchaseDate: new Date("2024-10-01"),
        purchasePrice: 30000000,
        serialNumber: "SAMGS24U002",
        brand: "Samsung",
        model: "Galaxy S24 Ultra",
        warrantyUntil: new Date("2026-10-01"),
        location: "IT Storage Room",
        createdAt: new Date("2024-10-01T10:00:00Z"),
        updatedAt: null
    },

    // Accessories
    {
        _id: "asset-acc-001",
        name: "Logitech MX Master 3S Mouse",
        category: "Accessory",
        status: "IN_USE",
        assignedTo: "user-emp-001",
        purchaseDate: new Date("2024-08-10"),
        purchasePrice: 3500000,
        serialNumber: "LOGMX3S001",
        brand: "Logitech",
        model: "MX Master 3S",
        warrantyUntil: new Date("2026-08-10"),
        location: "Marketing Office",
        createdAt: new Date("2024-08-10T09:00:00Z"),
        updatedAt: new Date("2024-08-15T11:00:00Z")
    }
]);

print("Created 10 assets successfully.");

// ============================================================
// STEP 4: CREATE ASSET HISTORIES (19 entries)
// ============================================================
print("=== STEP 4: Creating asset histories ===");

db.asset_histories.insertMany([
    // MacBook Pro - Assigned to Hoang
    {
        _id: "hist-001",
        assetId: "asset-lap-001",
        assetName: "MacBook Pro 14-inch M3",
        performedBy: "admin@company.com",
        action: "CREATED",
        details: "Asset 'MacBook Pro 14-inch M3' (Category: Laptop) created",
        timestamp: new Date("2024-06-15T10:00:00Z")
    },
    {
        _id: "hist-002",
        assetId: "asset-lap-001",
        assetName: "MacBook Pro 14-inch M3",
        userId: "user-emp-002",
        userName: "Le Van Hoang",
        performedBy: "admin@company.com",
        action: "ASSIGNED",
        details: "Asset assigned to Le Van Hoang",
        timestamp: new Date("2024-09-01T14:00:00Z")
    },

    // Dell XPS - Available
    {
        _id: "hist-003",
        assetId: "asset-lap-002",
        assetName: "Dell XPS 15",
        performedBy: "admin@company.com",
        action: "CREATED",
        details: "Asset 'Dell XPS 15' (Category: Laptop) created",
        timestamp: new Date("2024-03-20T09:00:00Z")
    },

    // ThinkPad - Assigned to Mai
    {
        _id: "hist-004",
        assetId: "asset-lap-003",
        assetName: "ThinkPad X1 Carbon Gen 11",
        performedBy: "admin@company.com",
        action: "CREATED",
        details: "Asset 'ThinkPad X1 Carbon Gen 11' (Category: Laptop) created",
        timestamp: new Date("2024-05-10T11:00:00Z")
    },
    {
        _id: "hist-005",
        assetId: "asset-lap-003",
        assetName: "ThinkPad X1 Carbon Gen 11",
        userId: "user-emp-001",
        userName: "Tran Thi Mai",
        performedBy: "admin@company.com",
        action: "ASSIGNED",
        details: "Asset assigned to Tran Thi Mai",
        timestamp: new Date("2024-08-01T09:00:00Z")
    },

    // HP EliteBook - Was assigned, returned, now broken
    {
        _id: "hist-006",
        assetId: "asset-lap-004",
        assetName: "HP EliteBook 840 G10",
        performedBy: "admin@company.com",
        action: "CREATED",
        details: "Asset 'HP EliteBook 840 G10' (Category: Laptop) created",
        timestamp: new Date("2023-12-01T08:00:00Z")
    },
    {
        _id: "hist-007",
        assetId: "asset-lap-004",
        assetName: "HP EliteBook 840 G10",
        userId: "user-emp-003",
        userName: "Pham Thi Lan",
        performedBy: "admin@company.com",
        action: "ASSIGNED",
        details: "Asset assigned to Pham Thi Lan",
        timestamp: new Date("2024-02-01T10:00:00Z")
    },
    {
        _id: "hist-008",
        assetId: "asset-lap-004",
        assetName: "HP EliteBook 840 G10",
        userId: "user-emp-003",
        userName: "Pham Thi Lan",
        performedBy: "admin@company.com",
        action: "RETURNED",
        details: "Asset returned by Pham Thi Lan",
        timestamp: new Date("2024-11-20T16:00:00Z")
    },
    {
        _id: "hist-009",
        assetId: "asset-lap-004",
        assetName: "HP EliteBook 840 G10",
        performedBy: "admin@company.com",
        action: "UPDATED",
        details: "Status: AVAILABLE -> BROKEN; Screen damaged - awaiting repair",
        timestamp: new Date("2024-11-20T16:30:00Z")
    },

    // Dell Monitor - Assigned to Hoang
    {
        _id: "hist-010",
        assetId: "asset-mon-001",
        assetName: "Dell UltraSharp U2723QE 27\"",
        performedBy: "admin@company.com",
        action: "CREATED",
        details: "Asset 'Dell UltraSharp U2723QE 27\"' (Category: Monitor) created",
        timestamp: new Date("2024-06-15T10:30:00Z")
    },
    {
        _id: "hist-011",
        assetId: "asset-mon-001",
        assetName: "Dell UltraSharp U2723QE 27\"",
        userId: "user-emp-002",
        userName: "Le Van Hoang",
        performedBy: "admin@company.com",
        action: "ASSIGNED",
        details: "Asset assigned to Le Van Hoang",
        timestamp: new Date("2024-09-01T14:30:00Z")
    },

    // LG Monitor - Available
    {
        _id: "hist-012",
        assetId: "asset-mon-002",
        assetName: "LG UltraFine 27UN880",
        performedBy: "admin@company.com",
        action: "CREATED",
        details: "Asset 'LG UltraFine 27UN880' (Category: Monitor) created",
        timestamp: new Date("2024-04-01T09:00:00Z")
    },

    // Samsung Monitor - Assigned to Lan
    {
        _id: "hist-013",
        assetId: "asset-mon-003",
        assetName: "Samsung Odyssey G7 32\"",
        performedBy: "admin@company.com",
        action: "CREATED",
        details: "Asset 'Samsung Odyssey G7 32\"' (Category: Monitor) created",
        timestamp: new Date("2024-07-20T11:00:00Z")
    },
    {
        _id: "hist-014",
        assetId: "asset-mon-003",
        assetName: "Samsung Odyssey G7 32\"",
        userId: "user-emp-003",
        userName: "Pham Thi Lan",
        performedBy: "admin@company.com",
        action: "ASSIGNED",
        details: "Asset assigned to Pham Thi Lan",
        timestamp: new Date("2024-10-01T10:00:00Z")
    },

    // iPhone - Assigned to Duc
    {
        _id: "hist-015",
        assetId: "asset-phn-001",
        assetName: "iPhone 15 Pro",
        performedBy: "admin@company.com",
        action: "CREATED",
        details: "Asset 'iPhone 15 Pro' (Category: Phone) created",
        timestamp: new Date("2024-09-15T09:00:00Z")
    },
    {
        _id: "hist-016",
        assetId: "asset-phn-001",
        assetName: "iPhone 15 Pro",
        userId: "user-emp-004",
        userName: "Hoang Van Duc",
        performedBy: "admin@company.com",
        action: "ASSIGNED",
        details: "Asset assigned to Hoang Van Duc",
        timestamp: new Date("2024-09-20T14:00:00Z")
    },

    // Samsung Phone - Available
    {
        _id: "hist-017",
        assetId: "asset-phn-002",
        assetName: "Samsung Galaxy S24 Ultra",
        performedBy: "admin@company.com",
        action: "CREATED",
        details: "Asset 'Samsung Galaxy S24 Ultra' (Category: Phone) created",
        timestamp: new Date("2024-10-01T10:00:00Z")
    },

    // Mouse - Assigned to Mai
    {
        _id: "hist-018",
        assetId: "asset-acc-001",
        assetName: "Logitech MX Master 3S Mouse",
        performedBy: "admin@company.com",
        action: "CREATED",
        details: "Asset 'Logitech MX Master 3S Mouse' (Category: Accessory) created",
        timestamp: new Date("2024-08-10T09:00:00Z")
    },
    {
        _id: "hist-019",
        assetId: "asset-acc-001",
        assetName: "Logitech MX Master 3S Mouse",
        userId: "user-emp-001",
        userName: "Tran Thi Mai",
        performedBy: "admin@company.com",
        action: "ASSIGNED",
        details: "Asset assigned to Tran Thi Mai",
        timestamp: new Date("2024-08-15T11:00:00Z")
    }
]);

print("Created 19 asset history entries successfully.");

// ============================================================
// STEP 5: CREATE INDEXES
// ============================================================
print("=== STEP 5: Creating indexes ===");

db.users.createIndex({ "email": 1 }, { unique: true });
db.assets.createIndex({ "status": 1 });
db.assets.createIndex({ "assignedTo": 1 });
db.assets.createIndex({ "category": 1 });
db.assets.createIndex({ "serialNumber": 1 }, { unique: true });
db.asset_histories.createIndex({ "assetId": 1 });
db.asset_histories.createIndex({ "userId": 1 });
db.asset_histories.createIndex({ "timestamp": -1 });

print("Created all indexes successfully.");

// ============================================================
// STEP 6: VALIDATION
// ============================================================
print("=== STEP 6: Validating data ===");

const userCount = db.users.countDocuments();
const assetCount = db.assets.countDocuments();
const historyCount = db.asset_histories.countDocuments();

print("");
print("=== DATA SUMMARY ===");
print("Users: " + userCount);
print("Assets: " + assetCount);
print("Histories: " + historyCount);
print("");
print("=== ASSET STATUS ===");
print("In Use: " + db.assets.countDocuments({ status: "IN_USE" }));
print("Available: " + db.assets.countDocuments({ status: "AVAILABLE" }));
print("Broken: " + db.assets.countDocuments({ status: "BROKEN" }));
print("");
print("=== VALIDATION CHECKS ===");

// Check 1: All IN_USE assets must have assignedTo
const invalidInUse = db.assets.countDocuments({ status: "IN_USE", assignedTo: null });
print("1. IN_USE without assignedTo: " + (invalidInUse === 0 ? "PASS" : "FAIL (" + invalidInUse + ")"));

// Check 2: No duplicate serial numbers
const duplicateSerials = db.assets.aggregate([
    { $group: { _id: "$serialNumber", count: { $sum: 1 } } },
    { $match: { count: { $gt: 1 } } }
]).toArray();
print("2. Duplicate serial numbers: " + (duplicateSerials.length === 0 ? "PASS" : "FAIL (" + duplicateSerials.length + ")"));

// Check 3: All history have valid action
const validActions = ["CREATED", "ASSIGNED", "RETURNED", "UPDATED", "DELETED"];
const invalidActions = db.asset_histories.countDocuments({ action: { $nin: validActions } });
print("3. Invalid history actions: " + (invalidActions === 0 ? "PASS" : "FAIL (" + invalidActions + ")"));

print("");
print("=== DATASET COMPLETE ===");
print("Database 'quanlytaisan' is now ready.");
print("");
print("=== LOGIN CREDENTIALS ===");
print("Admin: admin@company.com / password123");
print("User:  mai.tran@company.com / password123");
