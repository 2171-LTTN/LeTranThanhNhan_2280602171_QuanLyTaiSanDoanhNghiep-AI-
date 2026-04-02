# 📚 APPLY LESSONS LEARNED (Practical Guide)

## Purpose
Prevent repeating same mistakes. Apply learned patterns to new tasks.

---

## 🎯 How to Use This Guide

**BEFORE coding ANY task:**

Step 1: Read knowledge/02_lessons_learned.md
Step 2: Use this file to map lessons to current task
Step 3: Write code with lessons applied
Step 4: Verify lessons applied in code

---

## 📋 Lesson Application Process

### Step 1: Identify Relevant Lessons

**For Backend Task:**
- Read all lessons in lessons_learned.md
- Pick lessons that apply to current tech stack
- Example: "Lesson 2: Putting Logic in Controller" applies to all Java tasks

**For Frontend Task:**
- Pick React-specific lessons
- Example: "Lesson 9: Fetching Too Much Data" applies to API calls

### Step 2: Explicit Mapping

**Template: Before coding, write this checklist:**

```
LESSONS APPLIED IN THIS TASK:

Lesson 1: Input Validation
- [ ] Apply: Validate all inputs before processing
- [ ] Method: Use DTO validation + custom validators
- [ ] Check: Code has @Valid + try-catch

Lesson 2: Business Logic in Service Layer
- [ ] Apply: Move ALL business logic to Service
- [ ] Method: Controller only handles HTTP
- [ ] Check: Review Controller code (< 10 lines per method)

Lesson 4: JWT Configuration
- [ ] Apply: Proper JWT expiration + validation
- [ ] Method: Use SecurityConfig + JWT filter
- [ ] Check: JWT validation on every request

Lesson 9: Fetch Only Needed Data
- [ ] Apply: Use pagination + selects
- [ ] Method: @Query("SELECT new DTO(...)")
- [ ] Check: Response size reasonable
```

**Write this BEFORE coding, not after!**

---

## 🔥 Most Common Lessons

### For Backend (Java):

**Lesson 2: Logic in Controller**
```
❌ BAD:
@PostMapping("/register")
public ResponseEntity register(RegisterRequest req) {
    if (userRepository.existsByEmail(req.getEmail())) {
        throw new Exception();
    }
    User user = new User();
    user.setEmail(req.getEmail());
    user.setPasswordHash(bcrypt.hash(req.getPassword()));
    userRepository.save(user);
    return ResponseEntity.ok("Success");
}

✅ GOOD (After applying Lesson 2):
@PostMapping("/register")
public ResponseEntity register(RegisterRequest req) {
    User user = authService.register(req);
    return ResponseEntity.ok(new UserResponse(user));
}

// Logic in service:
public User register(RegisterRequest req) {
    validateInput(req);
    if (userRepository.existsByEmail(req.getEmail())) {
        throw new DuplicateEmailException();
    }
    User user = createUser(req);
    return userRepository.save(user);
}
```

**Check: If controller method > 5 lines (excluding braces), move to service!**

---

**Lesson 1: Input Validation**
```
❌ BAD:
public void register(String email, String password) {
    User user = new User();
    user.setEmail(email);
    user.setPassword(password);
    userRepository.save(user);
}

✅ GOOD (After applying Lesson 1):
@PostMapping("/register")
public ResponseEntity register(@Valid RegisterRequest req) {
    // DTO validation:
    // @Email
    // @Min(8) for password
    
    try {
        User user = authService.register(req);
        return ResponseEntity.ok(user);
    } catch (ValidationException e) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse(e.getMessage()));
    }
}

// In class:
public class RegisterRequest {
    @Email(message = "Invalid email")
    private String email;
    
    @Size(min = 8, message = "Password must be 8+ chars")
    private String password;
}
```

**Check: Every @PostMapping has @Valid on request DTO?**

---

**Lesson 3: Error Handling**
```
❌ BAD:
public User register(RegisterRequest req) {
    User user = new User(...);
    userRepository.save(user);
    return user;
}

✅ GOOD (After applying Lesson 3):
public User register(RegisterRequest req) {
    try {
        validateInput(req);
        User user = createUser(req);
        return userRepository.save(user);
    } catch (ValidationException e) {
        logger.error("Validation failed", e);
        throw new ApplicationException("Invalid input", e);
    } catch (Exception e) {
        logger.error("Unexpected error", e);
        throw new ApplicationException("Registration failed", e);
    }
}

// Global exception handler:
@ExceptionHandler(DuplicateEmailException.class)
public ResponseEntity handleDuplicate(DuplicateEmailException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ErrorResponse("Email already exists"));
}
```

**Check: Try-catch around risky operations? Global exception handler?**

---

### For Frontend (React):

**Lesson 9: Fetch Only Needed Data**
```
❌ BAD:
useEffect(() => {
    fetch('/api/assets')
        .then(res => res.json())
        .then(data => {
            // Loaded 1000 assets at once!
            setAssets(data);
        });
}, []);

✅ GOOD (After applying Lesson 9):
useEffect(() => {
    fetch('/api/assets?page=1&limit=20')  // Pagination!
        .then(res => res.json())
        .then(data => setAssets(data.content));
}, [currentPage]);
```

**Check: API calls use pagination? Ever load ALL data?**

---

## 🧠 Lesson Application Workflow

### Before Coding:

```
1. Read lessons_learned.md
   ↓
2. Create "LESSONS APPLIED" checklist
   ↓
3. Explicitly map lessons to current task
   ↓
4. Keep checklist visible while coding
```

### While Coding:

```
For each method/component:
- Check relevant lessons
- Apply them
- Mark in checklist
```

### After Coding:

```
1. Review code against lessons
2. Did I apply Lesson X? How?
3. Found violation? Fix immediately
4. Update bug_log.md if found issue
```

---

## ✅ Verification Checklist

**For EVERY method you write:**

```
LESSON CHECK:
- [ ] Lesson 1 (Input Validation): Valid inputs?
- [ ] Lesson 2 (Business Logic): Service layer has logic?
- [ ] Lesson 3 (Error Handling): Try-catch + meaningful errors?
- [ ] Lesson 4 (Security): Authenticated + authorized?
- [ ] Lesson 5 (DRY): Not duplicate code?
- [ ] Lesson 6 (Testing): Test this method?
- [ ] Lesson 9 (Performance): Only fetch needed data?

If ANY fails → FIX IMMEDIATELY
```

---

## 📝 Template for New Code

Use this template to write lessons INTO your code:

```java
// Lesson 2: Keep controller thin
// Business logic → Service layer
@PostMapping("/api/assets")
public ResponseEntity createAsset(@Valid CreateAssetRequest req) {
    // Lesson 1: Input validated by @Valid above
    
    try {
        // Lesson 2: Delegate to service
        Asset asset = assetService.create(req);
        
        // Lesson 3: Return proper response
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new AssetResponse(asset));
    } catch (ValidationException e) {
        // Lesson 3: Meaningful error message
        return ResponseEntity.badRequest()
            .body(new ErrorResponse(e.getMessage()));
    }
}
```

---

## 🎓 AI INSTRUCTION

**Before coding ANY method:**

1. Read knowledge/02_lessons_learned.md
2. Identify 3-5 relevant lessons
3. Write "LESSONS APPLIED" checklist
4. Code with lessons explicitly applied
5. After coding: Verify lessons applied
6. If lesson violated: FIX immediately

**Example:**

```
Task: Create UserController.register()

LESSONS TO APPLY:
- Lesson 1: Validate email + password
- Lesson 2: Move logic to UserService
- Lesson 3: Handle duplicate email error
- Lesson 5: Check for duplicate code elsewhere

WHILE CODING:
- Controller method ONLY calls userService.register()
- UserService does all validation + business logic
- Global exception handler catches errors
- Check: Is this logic duplicated elsewhere? NO

AFTER CODING:
- Read code: Did I apply Lesson 1? YES (DTO @Valid)
- Did I apply Lesson 2? YES (Service has logic)
- Did I apply Lesson 3? YES (Try-catch + error handler)
- Result: All lessons applied ✅
```

---

## Summary

> **Lessons Learned ≠ Just read once**
>
> **Lessons Learned = Apply to EVERY method**
>
> **Especially:** L1, L2, L3, L5, L6 (core lessons)
