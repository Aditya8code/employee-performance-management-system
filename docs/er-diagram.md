# Entity-Relationship (ER) Documentation & Diagram

## 1. Overview
The database schema for the **Employee Performance Management System** is designed in Third Normal Form (3NF) for **MySQL 8** (InnoDB engine), ensuring referential integrity, cascading updates/deletes where appropriate, and index optimization for analytical reporting.

---

## 2. Entity-Relationship Diagram

```mermaid
erDiagram
    USERS ||--o| EMPLOYEES : "has profile"
    USERS ||--o{ EMPLOYEES : "manages"
    USERS ||--o{ EVALUATIONS : "evaluates as manager"
    DEPARTMENTS ||--o{ EMPLOYEES : "belongs to"
    EVALUATION_CYCLES ||--o{ EVALUATIONS : "conducted during"
    EMPLOYEES ||--o{ EVALUATIONS : "appraised in"
    EVALUATIONS ||--|{ EVALUATION_SCORES : "contains"
    EVALUATION_CRITERIA ||--o{ EVALUATION_SCORES : "rated by"

    USERS {
        int id PK "AUTO_INCREMENT"
        string username UK "NOT NULL, VARCHAR(50)"
        string password_hash "NOT NULL, VARCHAR(255)"
        string role "NOT NULL, 'ADMIN'|'MANAGER'|'EMPLOYEE'"
        string full_name "NOT NULL, VARCHAR(100)"
        string email UK "NOT NULL, VARCHAR(100)"
        string phone "VARCHAR(20)"
        string status "NOT NULL, 'ACTIVE'|'INACTIVE'"
        timestamp created_at "DEFAULT CURRENT_TIMESTAMP"
        timestamp updated_at "DEFAULT CURRENT_TIMESTAMP ON UPDATE"
    }

    DEPARTMENTS {
        int id PK "AUTO_INCREMENT"
        string name UK "NOT NULL, VARCHAR(100)"
        text description
        timestamp created_at "DEFAULT CURRENT_TIMESTAMP"
    }

    EMPLOYEES {
        int id PK "AUTO_INCREMENT"
        int user_id UK, FK "NOT NULL, REFERENCES users(id)"
        int department_id FK "NOT NULL, REFERENCES departments(id)"
        int manager_id FK "NULL, REFERENCES users(id)"
        string job_title "NOT NULL, VARCHAR(100)"
        date hire_date "NOT NULL"
        decimal salary "DECIMAL(12,2)"
        timestamp created_at "DEFAULT CURRENT_TIMESTAMP"
        timestamp updated_at "DEFAULT CURRENT_TIMESTAMP ON UPDATE"
    }

    EVALUATION_CYCLES {
        int id PK "AUTO_INCREMENT"
        string name "NOT NULL, VARCHAR(100)"
        text description
        date start_date "NOT NULL"
        date end_date "NOT NULL"
        string status "NOT NULL, 'DRAFT'|'ACTIVE'|'COMPLETED'"
        timestamp created_at "DEFAULT CURRENT_TIMESTAMP"
        timestamp updated_at "DEFAULT CURRENT_TIMESTAMP ON UPDATE"
    }

    EVALUATION_CRITERIA {
        int id PK "AUTO_INCREMENT"
        string name UK "NOT NULL, VARCHAR(100)"
        text description
        decimal weight "NOT NULL, DECIMAL(5,2)"
        boolean is_active "NOT NULL, DEFAULT TRUE"
        timestamp created_at "DEFAULT CURRENT_TIMESTAMP"
    }

    EVALUATIONS {
        int id PK "AUTO_INCREMENT"
        int employee_id FK "NOT NULL, REFERENCES employees(id)"
        int manager_id FK "NOT NULL, REFERENCES users(id)"
        int cycle_id FK "NOT NULL, REFERENCES evaluation_cycles(id)"
        decimal total_score "DECIMAL(4,2), 1.00-5.00"
        string rating_label "VARCHAR(50)"
        text overall_feedback
        text strengths
        text improvements
        string status "NOT NULL, 'DRAFT'|'SUBMITTED'|'APPROVED'"
        timestamp submitted_at
        timestamp created_at "DEFAULT CURRENT_TIMESTAMP"
        timestamp updated_at "DEFAULT CURRENT_TIMESTAMP ON UPDATE"
    }

    EVALUATION_SCORES {
        int id PK "AUTO_INCREMENT"
        int evaluation_id FK "NOT NULL, REFERENCES evaluations(id)"
        int criterion_id FK "NOT NULL, REFERENCES evaluation_criteria(id)"
        int score "NOT NULL, CHECK(1 <= score <= 5)"
        text comments
    }
```

---

## 3. Data Dictionary

### 3.1. `users` Table
- Stores base authentication credentials, personal contact info, and role assignment.
- Roles: `ADMIN`, `MANAGER`, `EMPLOYEE`.
- Passwords stored as 60-character salted BCrypt hashes.

### 3.2. `departments` Table
- Organizational divisions (e.g., Engineering, Sales & Marketing, Human Resources).
- Restricts deletion if employees are currently assigned to the department.

### 3.3. `employees` Table
- Extends a user account with employment specifics: job title, hire date, salary, department FK, and manager FK.
- `user_id` has a 1-to-1 unique relationship with `users(id)` and cascades on delete.

### 3.4. `evaluation_cycles` Table
- Manages performance evaluation timelines.
- Status values:
  - `DRAFT`: Cycle is created but not yet open for manager submissions.
  - `ACTIVE`: Open for managers to score and submit appraisals.
  - `COMPLETED`: Cycle closed; archived for reporting and history viewing.

### 3.5. `evaluation_criteria` Table
- Stores appraisal assessment criteria and percentage weightage:
  1. Attendance (15%)
  2. Work Quality (20%)
  3. Productivity (20%)
  4. Teamwork (15%)
  5. Communication (15%)
  6. Goal Achievement (15%)
  Sum = 100%.

### 3.6. `evaluations` Table
- Master appraisal record for an employee in a given evaluation cycle.
- Enforces unique constraint `(employee_id, cycle_id)` so an employee receives one evaluation per cycle.
- `total_score` and `rating_label` are calculated based on weighted criteria scores.

### 3.7. `evaluation_scores` Table
- Individual score (1–5) and feedback comments for each criterion in an evaluation.
- Enforces unique constraint `(evaluation_id, criterion_id)`.
