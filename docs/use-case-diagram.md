# Use Case Documentation & Diagram

## 1. Overview
The **Employee Performance Management System (EPS)** is an enterprise web application designed to facilitate end-to-end performance appraisals. The system defines three primary human actors: **System Administrator**, **Manager / Lead Evaluator**, and **Employee**, as well as automated system services.

---

## 2. Actors & Responsibilities

| Actor | Description | Key Responsibilities |
| :--- | :--- | :--- |
| **Admin** | System administrator with full privileges | Manages departments, employees, criteria weightages, appraisal cycles, and company-wide performance reports. |
| **Manager** | People manager / supervisor | Reviews assigned direct reports, scores competencies (1–5), records qualitative feedback, saves drafts, and submits final evaluations. |
| **Employee** | Staff member being evaluated | Views personal dashboard, appraisal history across review cycles, detailed criteria scores, competency radar charts, and manager development recommendations. |
| **System** | Background authentication and calculation service | Enforces session security, verifies BCrypt hashes, calculates weighted scores, maps rating categories, and generates CSV exports. |

---

## 3. Use Case Diagram

```mermaid
flowchart LR
    subgraph Actors
        A["Admin"]
        M["Manager"]
        E["Employee"]
    end

    subgraph Authentication_Security ["Authentication & Security"]
        UC_LOGIN["Login with BCrypt Verification"]
        UC_LOGOUT["Logout & Invalidate Session"]
        UC_AUTH_FILTER["Enforce Role Authorization"]
    end

    subgraph Admin_Use_Cases ["Administrative Management"]
        UC_DASH_ADMIN["View Org Dashboard & KPIs"]
        UC_DEPT_CRUD["Manage Departments (CRUD)"]
        UC_EMP_CRUD["Manage Employees (CRUD)"]
        UC_ASSIGN_MGR["Assign Reporting Managers"]
        UC_CRIT_CRUD["Configure Criteria & Weights"]
        UC_CYCLE_CRUD["Manage Evaluation Cycles"]
        UC_CYCLE_STATUS["Transition Cycle Status"]
        UC_REPORTS["View Org Performance Reports"]
        UC_CSV_EXPORT["Export Reports to CSV"]
    end

    subgraph Manager_Use_Cases ["Manager Appraisal Flow"]
        UC_DASH_MGR["View Manager Dashboard"]
        UC_TEAM_VIEW["View Assigned Direct Reports"]
        UC_EVAL_START["Initiate Employee Evaluation"]
        UC_SCORE["Score Dimensions (1-5) & Comments"]
        UC_LIVE_CALC["Live Weighted Score Preview"]
        UC_DRAFT["Save Appraisal as Draft"]
        UC_SUBMIT["Submit Final Appraisal"]
        UC_VIEW_EVAL["View Submitted Appraisal"]
    end

    subgraph Employee_Use_Cases ["Employee Self-Service"]
        UC_DASH_EMP["View Employee Dashboard"]
        UC_LATEST_RATING["View Latest Score & Rating Badge"]
        UC_HIST["View Personal Appraisal History"]
        UC_RADAR["View Competency Radar Chart"]
        UC_FEEDBACK["Read Manager Feedback & Strengths"]
        UC_PRINT["Print Appraisal Report"]
    end

    %% Actor Relationships
    A --> UC_LOGIN
    A --> UC_LOGOUT
    A --> UC_DASH_ADMIN
    A --> UC_DEPT_CRUD
    A --> UC_EMP_CRUD
    A --> UC_ASSIGN_MGR
    A --> UC_CRIT_CRUD
    A --> UC_CYCLE_CRUD
    A --> UC_CYCLE_STATUS
    A --> UC_REPORTS
    A --> UC_CSV_EXPORT

    M --> UC_LOGIN
    M --> UC_LOGOUT
    M --> UC_DASH_MGR
    M --> UC_TEAM_VIEW
    M --> UC_EVAL_START
    M --> UC_SCORE
    M --> UC_DRAFT
    M --> UC_SUBMIT
    M --> UC_VIEW_EVAL

    E --> UC_LOGIN
    E --> UC_LOGOUT
    E --> UC_DASH_EMP
    E --> UC_LATEST_RATING
    E --> UC_HIST
    E --> UC_RADAR
    E --> UC_FEEDBACK
    E --> UC_PRINT

    %% Includes & Extends
    UC_SCORE -.->|includes| UC_LIVE_CALC
    UC_SUBMIT -.->|triggers| UC_AUTH_FILTER
```

---

## 4. Key Use Case Descriptions

### UC-01: Authenticate User
- **Actor**: Admin, Manager, Employee
- **Precondition**: User is registered with status `ACTIVE`.
- **Main Flow**:
  1. User submits username/email and plain-text password.
  2. Filter permits unauthenticated access to `/auth/login`.
  3. `AuthService` queries user by username or email.
  4. Password is verified using BCrypt hash check.
  5. Upon success, HTTP session is initialized and populated with `currentUser`.
  6. User is redirected to their polymorphic role dashboard.
- **Exceptions**: Invalid credentials or inactive account displays appropriate flash error message.

### UC-02: Conduct Performance Evaluation
- **Actor**: Manager
- **Precondition**: An evaluation cycle is in `ACTIVE` status and employee reports to manager.
- **Main Flow**:
  1. Manager selects employee from team list.
  2. System loads the 6 configured criteria: Attendance, Work Quality, Productivity, Teamwork, Communication, Goal Achievement.
  3. Manager selects a 1–5 score for each dimension and enters comments.
  4. System computes real-time preview of weighted score:
     $$\text{Total Score} = \frac{\sum (\text{Score}_i \times \text{Weight}_i)}{\sum \text{Weight}_i}$$
  5. Manager enters overall qualitative feedback, strengths, and development areas.
  6. Manager chooses "Save as Draft" or "Submit Final Evaluation".
  7. Final submission updates evaluation status to `SUBMITTED`, sets `submitted_at`, and records rating category.
