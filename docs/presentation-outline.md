# Employee Performance Management System (EPS)
## Project Presentation & System Walkthrough Outline

---

### Slide 1: Title & Project Overview
- **Title**: Enterprise Employee Performance Management System (EPS)
- **Subtitle**: Role-Based Appraisal Lifecycle, Competency Scoring, and Organizational Analytics
- **Technology Stack**: Java 17, Maven, Jakarta EE 10 Servlets, JSP, JSTL, MySQL 8, JDBC, Bootstrap 5, Chart.js (MVC Architecture)
- **Presenter**: Engineering Team

---

### Slide 2: Problem Statement & Motivation
- **Challenges in Traditional Reviews**:
  - Disconnected spreadsheets and subjective appraisals without clear weighting.
  - Lack of timely visibility for managers into pending team evaluations.
  - Opaque appraisal outcomes for employees without actionable feedback.
  - Manual reporting bottlenecks for HR and leadership.
- **The Solution**:
  - A centralized, secure, role-based appraisal management platform providing structured evaluations, automated weighted scoring, live calculation previews, and self-service dashboards.

---

### Slide 3: Core Features by Role
- **Administrator**:
  - Real-time KPI dashboard (headcount, active cycle status, company average score).
  - Department management with staffing constraints.
  - Employee directory with manager supervisor assignments.
  - Appraisal criteria configuration with customizable weights (summing to 100%).
  - Review cycle lifecycle transitions (DRAFT $\to$ ACTIVE $\to$ COMPLETED).
  - Cross-departmental analytics and one-click CSV report streaming.
- **Manager**:
  - Team roster with live progress tracking (Submitted, Draft, Not Started).
  - Interactive appraisal form with real-time score calculator preview.
  - 1–5 scoring across 6 key dimensions + criterion-specific notes.
  - Qualitative feedback (Strengths, Development Areas, Executive Remarks).
  - Draft saving and final submission controls.
- **Employee**:
  - Personal performance dashboard with latest rating badge and score.
  - Full appraisal history across past review cycles.
  - Detailed report breakdown with interactive Chart.js Competency Radar Chart.
  - Direct manager guidance and development recommendations.

---

### Slide 4: System Architecture & Technology Stack
- **Architecture**: Clean 3-Tier Model-View-Controller (MVC) Pattern.
- **Backend**:
  - Java 17 LTS
  - Jakarta EE 10 Servlets (`jakarta.servlet.*`)
  - Jakarta Standard Tag Library (JSTL 3.0)
  - Pure JDBC with `PreparedStatement` (Zero Spring Boot)
- **Frontend**:
  - JavaServer Pages (JSP)
  - Responsive Bootstrap 5.3 & Bootstrap Icons
  - Chart.js for Bar, Doughnut, and Radar visual analytics
- **Database**:
  - MySQL 8.0 (InnoDB, foreign keys, cascade rules, indexing)
  - Smart zero-setup embedded fallback for automated testing environments

---

### Slide 5: Database Schema & Entity Relationships
- **Normalized Tables**:
  1. `users`: Authentication, personal details, role (`ADMIN`, `MANAGER`, `EMPLOYEE`).
  2. `departments`: Business unit structure.
  3. `employees`: Extends user with department, manager, job title, hire date, salary.
  4. `evaluation_cycles`: Appraisal windows and status transitions.
  5. `evaluation_criteria`: Dimensions and percentage weights.
  6. `evaluations`: Appraisal master header and final scores.
  7. `evaluation_scores`: Criterion-level ratings (1–5) and feedback comments.
- **Data Integrity**: Foreign key constraints, unique constraints on `(employee_id, cycle_id)`, and `CHECK` score range constraints.

---

### Slide 6: Object-Oriented Programming (OOP) in Practice
- **Abstraction**: Abstract `User` base class with concrete polymorphic subclasses `Admin`, `Manager`, `Employee`.
- **Inheritance**: `Employee extends User` inherits account attributes and adds employment details.
- **Polymorphism**: `getRoleDisplayName()` and `getDefaultDashboardUrl()` dynamically route users and render role-specific views.
- **Encapsulation**: Private fields with accessors; domain calculation methods (`calculateTotalScore()`, `determineRating()`).
- **Generics**: Generic `GenericDAO<T, ID>` interface standardizing type-safe persistence.
- **Custom Exceptions**: Typed exception hierarchy (`AuthenticationException`, `ValidationException`, `ResourceNotFoundException`, `DatabaseException`).

---

### Slide 7: Scoring Formula & Official Rating Bands
- **Calculation Formula**:
  $$\text{Total Score} = \frac{\sum (\text{Score}_i \times \text{Weight}_i)}{\sum \text{Weight}_i}$$
- **Rating Bands**:
  - **4.50 – 5.00**: Outstanding *(Green)*
  - **3.50 – 4.49**: Exceeds Expectations *(Blue)*
  - **2.50 – 3.49**: Meets Expectations *(Cyan)*
  - **1.50 – 2.49**: Needs Improvement *(Yellow)*
  - **Below 1.50**: Unsatisfactory *(Red)*

---

### Slide 8: Security Implementation
- **BCrypt Hashing**: Passwords stored as 12-round salted BCrypt hashes.
- **Filter-Based Access Control**:
  - `EncodingFilter`: Ensures consistent UTF-8 encoding.
  - `AuthenticationFilter`: Intercepts unauthenticated sessions and redirects to sign-in.
  - `RoleAuthorizationFilter`: Restricts `/admin/*`, `/manager/*`, and `/employee/*` to authorized roles.
- **Session Security**: Session invalidation upon logout; HTTP-only cookies; anti-caching HTTP headers.

---

### Slide 9: Live Demo Walkthrough
1. **Admin Persona** (`admin` / `Admin@123`):
   - Review organization dashboard KPIs and department averages.
   - Inspect evaluation criteria and weights (100% total).
   - Review and export company-wide reports to CSV.
2. **Manager Persona** (`manager1` / `Manager@123`):
   - View assigned engineering direct reports.
   - Select an employee in the active cycle.
   - Adjust criterion sliders/radios and observe real-time score updates.
   - Save draft and submit final appraisal.
3. **Employee Persona** (`emp1` / `Employee@123`):
   - Access personal performance portal.
   - Inspect latest appraisal score and rating badge.
   - View 6-axis competency radar chart and historical review trends.

---

### Slide 10: Business Impact & Conclusion
- **Efficiency**: Reduces appraisal review completion cycles from weeks to days.
- **Transparency**: Clear criteria and weighted scoring build employee trust.
- **Extensibility**: Modular architecture allows future extensions (Self-appraisals, 360-degree feedback, PDF export, SSO/OAuth2).
- **Q&A**: Open for questions.
