# Employee Performance Management System (EPS)

Enterprise-grade Employee Performance Management & Appraisal System built with **Java 17**, **Maven**, **Jakarta Servlets (Tomcat 10+)**, **JSP & JSTL**, **MySQL 8**, **JDBC**, **Bootstrap 5**, and **Chart.js**. The application strictly implements the **Model-View-Controller (MVC)** architectural pattern with **zero Spring Boot dependencies**.

---

## 🌟 Key Features

### 🛡️ Role-Based Access & Security
- **Secure Authentication**: Salted **BCrypt** password hashing (cost factor 12).
- **Session Management**: Secure session validation with cache-prevention headers.
- **Filter-Based Security**: Declarative `AuthenticationFilter` and `RoleAuthorizationFilter` enforcing strict boundaries for `/admin/*`, `/manager/*`, and `/employee/*`.
- **Demo Quick Access**: One-click demo credential fill on the sign-in screen.

### 👑 Administrator Features
- **Organization Dashboard**: KPI summary cards (Headcount, Active Cycle, Completed Appraisals, Company Average Score), Department chart, and quick shortcuts.
- **Departments Management (CRUD)**: Create, edit, and delete departments with safety checks preventing deletion of populated departments.
- **Employees Management (CRUD)**: Register staff with user account generation, edit employee profiles, assign reporting supervisors, and delete staff.
- **Criteria & Weightage Configuration**: Configure appraisal criteria and relative weightages (Attendance, Work Quality, Productivity, Teamwork, Communication, Goal Achievement) with visual 100% total progress tracker.
- **Evaluation Cycles Lifecycle**: Manage appraisal cycles (Start date, End date, Description) and state transitions (`DRAFT` $\to$ `ACTIVE` $\to$ `COMPLETED`).
- **Organization Performance Reports**:
  - Filter by Cycle, Department, Rating category, and Status.
  - Interactive **Chart.js Bar Chart** (Department average scores).
  - Interactive **Chart.js Doughnut Chart** (Rating band distribution).
  - **CSV Export**: Streamed directly via HTTP with proper attachment headers.

### 👔 Manager Features
- **Manager Dashboard**: Overview of direct reports, pending appraisal counts, completed counts, and active cycle alerts.
- **Team Appraisal Roster**: Track review status per team member (`Submitted`, `Draft Saved`, `Not Started`).
- **Performance Appraisal Form**:
  - 1–5 scoring across all 6 core criteria with specific comment fields.
  - **Live Score Calculator**: Real-time weighted score preview and rating badge updater as scores are selected.
  - Qualitative assessments: Key Strengths, Areas for Improvement, and Overall Summary Feedback.
  - Flexible workflow: **Save as Draft** or **Submit Final Evaluation**.
- **Appraisal Review View**: Clean, printable summary record of submitted evaluations.

### 👤 Employee Features
- **Self-Service Dashboard**: Personal profile, latest appraisal rating badge, and overall score card.
- **Performance Score History**: Interactive line chart tracking performance scores across past appraisal cycles.
- **Appraisal History**: Complete archive of all past appraisals.
- **Detailed Appraisal Breakdown**:
  - Individual criterion scores (1–5), weights, and manager feedback.
  - **Chart.js Competency Radar Chart** mapping strengths across the 6 dimensions.
  - Manager's guidance on development goals and strengths.
  - Print / PDF export friendly view.

---

## 📐 Scoring Formula & Official Rating Scale

Appraisals are scored on a scale of **1.00 to 5.00** using the weighted formula:

$$\text{Total Score} = \frac{\sum_{i=1}^{n} (\text{Score}_i \times \text{Weight}_i)}{\sum_{i=1}^{n} \text{Weight}_i}$$

### Official Rating Categories:
| Score Range | Performance Rating | Badge Style |
| :--- | :--- | :--- |
| **4.50 – 5.00** | **Outstanding** | `bg-success` (Green) |
| **3.50 – 4.49** | **Exceeds Expectations** | `bg-primary` (Blue) |
| **2.50 – 3.49** | **Meets Expectations** | `bg-info` (Cyan) |
| **1.50 – 2.49** | **Needs Improvement** | `bg-warning` (Yellow) |
| **Below 1.50** | **Unsatisfactory** | `bg-danger` (Red) |

### Default Criteria & Weights:
1. **Attendance & Punctuality**: 15.00%
2. **Work Quality & Accuracy**: 20.00%
3. **Productivity & Timeliness**: 20.00%
4. **Teamwork & Collaboration**: 15.00%
5. **Communication Skills**: 15.00%
6. **Goal Achievement**: 15.00%
*(Total Sum = 100.00%)*

---

## 🔑 Demo User Credentials

All sample accounts are preloaded with BCrypt hashed passwords:

| Role | Username | Email | Password | Assigned Department / Notes |
| :--- | :--- | :--- | :--- | :--- |
| **Admin** | `admin` | `admin@company.com` | `Admin@123` | System Administrator |
| **Manager 1** | `manager1` | `manager1@company.com` | `Manager@123` | Engineering Manager (Sarah Jenkins) |
| **Manager 2** | `manager2` | `manager2@company.com` | `Manager@123` | Sales & Marketing Manager (David Miller) |
| **Employee 1** | `emp1` | `alice.zhang@company.com` | `Employee@123` | Senior Backend Engineer (Reports to Manager 1) |
| **Employee 2** | `emp2` | `bob.martin@company.com` | `Employee@123` | Frontend Developer (Reports to Manager 1) |
| **Employee 3** | `emp3` | `charlie.brown@company.com` | `Employee@123` | Full Stack Engineer (Reports to Manager 1) |
| **Employee 4** | `emp4` | `diana.prince@company.com` | `Employee@123` | QA Automation Engineer (Reports to Manager 1) |
| **Employee 5** | `emp5` | `ethan.hunt@company.com` | `Employee@123` | DevOps & Cloud Engineer (Reports to Manager 1) |
| **Employee 6** | `emp6` | `fiona.gallagher@company.com` | `Employee@123` | Account Executive (Reports to Manager 2) |
| **Employee 7** | `emp7` | `george.clark@company.com` | `Employee@123` | Digital Marketing Specialist (Reports to Manager 2) |
| **Employee 8** | `emp8` | `hannah.abbott@company.com` | `Employee@123` | Sales Development Rep (Reports to Manager 2) |
| **Employee 9** | `emp9` | `ian.malcolm@company.com` | `Employee@123` | Content Strategist (Reports to Manager 2) |
| **Employee 10** | `emp10` | `julia.roberts@company.com` | `Employee@123` | HR Generalist (Reports to Manager 2) |

---

