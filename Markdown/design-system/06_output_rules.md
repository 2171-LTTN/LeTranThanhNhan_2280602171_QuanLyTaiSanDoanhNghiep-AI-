# 📤 OUTPUT RULES (Code Generation Standard)

## Purpose
Ensure all generated code follows project standards and is production-ready.

---

## 🎯 Core Rules

### Rule 1: Code MUST be Runnable

Before output:
- ✅ Code compiles without syntax errors
- ✅ All imports are correct
- ✅ All dependencies are available
- ✅ No missing closing brackets/parentheses
- ✅ Code can execute without compilation errors

**If fails:** Do NOT output, fix first.

---

### Rule 2: Code MUST be Complete

No partial or stub implementations:

❌ **BAD:**
```java
public class UserService {
    public void register(RegisterRequest req) {
        // TODO: implement
    }
}
```

✅ **GOOD:**
```java
public class UserService {
    public void register(RegisterRequest req) {
        validateInput(req);
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPasswordHash(bcrypt.hash(req.getPassword()));
        userRepository.save(user);
    }
}
```

---

### Rule 3: Code MUST Follow Project Structure

Before output, verify:
- ✅ **Location**: File in correct package/folder?
- ✅ **Naming**: Class/method names follow conventions?
- ✅ **Imports**: Using project's standard libraries?
- ✅ **Dependencies**: Using project's existing code?
- ✅ **Format**: Matches project's code style?

Example structure:
```
Backend (Spring Boot)
├── controller/     ← HTTP handlers
├── service/        ← Business logic
├── repository/     ← Data access
├── entity/         ← Database models
├── dto/            ← Request/Response objects
├── util/           ← Utilities
└── config/         ← Configuration

Frontend (React)
├── components/     ← React components
├── pages/          ← Page components
├── services/       ← API calls
├── hooks/          ← Custom hooks
├── utils/          ← Utilities
└── styles/         ← CSS/Tailwind
```

---

### Rule 4: Code MUST Follow Coding Standards

Before output, check:
- ✅ **Class size**: < 200 lines?
- ✅ **Method size**: < 50 lines?
- ✅ **Naming**: Meaningful names (not x, temp, data1)?
- ✅ **Validation**: Input validated?
- ✅ **Errors**: Exceptions handled?
- ✅ **Comments**: WHY explained (not WHAT)?
- ✅ **Duplication**: No copy-paste code?

---

### Rule 5: Code MUST Include Explanation

BEFORE any code block:

```
Explanation:
- What this does
- Why we need it
- Key logic points
```

Then:
```
Code:
[Actual code here]
```

Example:
```
EXPLANATION:
AuthController handles user registration and login endpoints.
It receives requests, validates input, and delegates to AuthService.
It should NOT contain business logic (only HTTP handling).

CODE:
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    ...
}
```

---

### Rule 6: Code MUST be Tested

BEFORE output:
- ✅ Logic verified mentally
- ✅ Edge cases considered
- ✅ Error handling included
- ✅ Test cases outlined (if required)

If API:
```
Testing approach:
- POST /api/auth/register with valid data → Should create user
- POST /api/auth/register with duplicate email → Should return error
- POST /api/auth/register with invalid email → Should return validation error
```

---

### Rule 7: Code Output Template

All code should follow this template:

```
## [Feature Name]

### Purpose
[What this feature does]

### Location
[Package/folder: com.example.service.AuthService]

### Explanation
[How it works, key logic]

### Code

\`\`\`java
[Actual code]
\`\`\`

### Testing
[How to test this code]

### Integration
[How to integrate with other components]
```

---

## 📋 Output Checklist

Before marking code as ready:

```
CODE QUALITY:
- [ ] Compiles without errors?
- [ ] Runs without exceptions?
- [ ] Follows naming conventions?
- [ ] No code duplication?
- [ ] Error handling included?
- [ ] Input validation included?

COMPLETENESS:
- [ ] All methods implemented (not stubs)?
- [ ] All imports correct?
- [ ] All dependencies available?
- [ ] No TODO/FIXME comments?

STANDARDS:
- [ ] Follows design-system?
- [ ] Follows coding-standards?
- [ ] Following architecture (MVC)?
- [ ] File in correct location?

DOCUMENTATION:
- [ ] Has explanation (BEFORE code)?
- [ ] Has testing approach?
- [ ] Has integration notes?
- [ ] Comments explain WHY?

TESTABILITY:
- [ ] Can be tested immediately?
- [ ] Test cases clear?
- [ ] Edge cases considered?
```

---

## 🚀 Output Format Summary

**ALWAYS use this format:**

```
EXPLANATION:
[2-3 sentences about what, why, how]

CODE:
[Complete, runnable, tested code]

TESTING:
[How to verify it works]

NOTES:
[Integration steps, dependencies]
```

---

## ❌ What NOT to Output

**DO NOT output:**

1. ❌ Incomplete code (stubs, TODO, FIXME)
2. ❌ Code that doesn't compile
3. ❌ Code outside project structure
4. ❌ Code without explanation
5. ❌ Code without error handling
6. ❌ Code without comments
7. ❌ Duplicate of existing code
8. ❌ Code violating design principles
9. ❌ Code without testing approach
10. ❌ Untested code examples

**If any applies:** REWRITE before outputting

---

## 🧠 AI INSTRUCTION

Before generating ANY code:

```
Checklist:
1. [ ] Understand requirements fully
2. [ ] Plan logic/flow
3. [ ] Know where to put file
4. [ ] Know what to import
5. [ ] Write explanation first
6. [ ] Write complete code
7. [ ] Verify it compiles (mentally)
8. [ ] Verify error handling
9. [ ] Add testing approach
10. [ ] Output formatted correctly

If ANY unchecked → DO NOT OUTPUT

After output:
- User will test immediately
- If fails → Your fault
```

---

## Examples

### ✅ GOOD Output Format

```
## User Entity Creation

### Purpose
Define the User entity for MongoDB and map to the users collection.

### Explanation
The User entity represents a user in the system.
It includes fields for identification, authentication, and role-based access.
We use @Document for MongoDB mapping and @Id for primary key.

### Code

\`\`\`java
@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String email;
    
    private String name;
    private String passwordHash;
    private String role;
    private LocalDateTime createdAt;
    
    public User(String name, String email, String passwordHash) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = "USER";
        this.createdAt = LocalDateTime.now();
    }
}
\`\`\`

### Testing

1. Create new User instance
2. Save to MongoDB
3. Retrieve by email
4. Verify all fields

### Notes
- Use @Data from Lombok to reduce boilerplate
- Email field is unique to prevent duplicates
- CreatedAt set automatically
```

### ❌ BAD Output Format

```
Just write:

@Document
public class User {
    // TODO: implement fields
    // add more later
}
```

**Why bad:**
- Not complete
- Has TODO
- Not testable
- No explanation
- No structure

---

## Summary

> **Good output = Complete + Tested + Explained + Formatted**
>
> **Bad output = Incomplete + Untested + No explanation + Wrong format**
>
> **It's your job to produce GOOD output every time**
