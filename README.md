# RevLang

This Language is a simple, custom programming language with a C-like syntax. This document provides an overview of the language's syntax, keywords, and rules to help users understand and use it effectively.

---

## **1. Keywords**
My Language has the following reserved keywords:

| Keyword  | Meaning |
|----------|---------|
| `tni`    | Defines an integer variable |
| `taolf`  | Defines a floating-point variable |
| `rahc`   | Defines a character variable |
| `loob`   | Defines a boolean variable |
| `tuoc`   | Print on console |
| `nic`    | Input from console |
| `sey`    | Boolean value `true` |
| `on`     | Boolean value `false` |

---

## **2. Identifiers**
Identifiers in My Language:
- Can contain **letters, digits, and underscores (`_`)**.
- Must **start with a letter or an underscore** (e.g., `_var`, `my_variable`, `abc_12`).
- Are **case-sensitive**.
- Only LowerCased allowed
- Cannot be a reserved keyword.

**Examples:**
```c
tni myvar = 10; // Valid
taolf _varname = 20; // Valid
tni 5number = 30; // Invalid (Cannot start with a digit)
rahc myVar = 'a'; // Invalid (Cannot contain uppercase)
```

---

## **3. Data Types**
| Data Type  | Example |
|------------|---------|
| Integer (`tni`) | `tni x = 10;` |
| Float (`taolf`) | `taolf y = 10.5;` |
| Character (`rahc`) | `rahc ch = 'A';` |
| Boolean (`loob`) | `loob flag = sey;` |

---

## **4. Operators**
| Operator | Meaning |
|----------|---------|
| `+`      | Addition |
| `-`      | Subtraction |
| `*`      | Multiplication |
| `/`      | Division |
| `%`      | Modulus |
| `^`      | Exponentiation |
| `=`      | Assignment |

---

## **5. Comments**
- **Single-line comments:** Start with `//`
- **Multi-line comments:** Enclosed in `/* ... */`

**Example:**
```c
// This is a single-line comment
/* This is a
   multi-line comment */
```

---

## **6. Example Program**
```c
tni abc = 10 + 200;
taolf b = 20.56453714^3;
taolf var = 20.564/3.24;
tni remainder = 20%6;
loob flag_24 = on;
tuoc flag_24;
nic flag_24;
rahc c = 'x';
```

---

## **7. Error Handling**
- **Unclosed character literals**: `'a`
- **Uppercase identifiers**: `A` 
- **Invalid identifier names**: `5var` 
- **Mismatched comment closure**: `/* comment`

---
