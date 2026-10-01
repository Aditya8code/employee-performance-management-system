-- ============================================================================
-- Employee Performance Evaluation System - Realistic Sample Data
-- ============================================================================

USE eps_db;

-- 1. Insert Departments
INSERT INTO departments (id, name, description) VALUES
(1, 'Engineering', 'Software development, QA testing, DevOps and infrastructure engineering'),
(2, 'Sales & Marketing', 'Inbound/outbound sales, demand generation, and brand marketing'),
(3, 'Human Resources', 'Talent acquisition, employee engagement, and organizational development');

-- 2. Insert Users
-- Passwords:
-- Admin:    Admin@123    -> $2a$12$Wkyj/KUtF54zptPOwZnLE.Aal0DJJjFI7tNz7TfP8tcGcNwRtldgu
-- Managers: Manager@123  -> $2a$12$.H9drDdby2XWHRlHkb2fjuDEsJAnnjqeciTik.0GR80N1Gk3hnIGS
-- Employees: Employee@123 -> $2a$12$fGgRGGrFRxsnMEFB3TS.jug5Ik//1xRQVLOnmgOB0q.7IY6.k2Bzq

INSERT INTO users (id, username, password_hash, role, full_name, email, phone, status) VALUES
-- Admin
(1, 'admin', '$2a$12$Wkyj/KUtF54zptPOwZnLE.Aal0DJJjFI7tNz7TfP8tcGcNwRtldgu', 'ADMIN', 'Alexander Davis (Admin)', 'admin@company.com', '+1-555-0100', 'ACTIVE'),

-- Managers
(2, 'manager1', '$2a$12$.H9drDdby2XWHRlHkb2fjuDEsJAnnjqeciTik.0GR80N1Gk3hnIGS', 'MANAGER', 'Sarah Jenkins', 'manager1@company.com', '+1-555-0201', 'ACTIVE'),
(3, 'manager2', '$2a$12$.H9drDdby2XWHRlHkb2fjuDEsJAnnjqeciTik.0GR80N1Gk3hnIGS', 'MANAGER', 'David Miller', 'manager2@company.com', '+1-555-0202', 'ACTIVE'),

-- 10 Employees
(4, 'emp1', '$2a$12$fGgRGGrFRxsnMEFB3TS.jug5Ik//1xRQVLOnmgOB0q.7IY6.k2Bzq', 'EMPLOYEE', 'Alice Zhang', 'alice.zhang@company.com', '+1-555-0301', 'ACTIVE'),
(5, 'emp2', '$2a$12$fGgRGGrFRxsnMEFB3TS.jug5Ik//1xRQVLOnmgOB0q.7IY6.k2Bzq', 'EMPLOYEE', 'Bob Martin', 'bob.martin@company.com', '+1-555-0302', 'ACTIVE'),
(6, 'emp3', '$2a$12$fGgRGGrFRxsnMEFB3TS.jug5Ik//1xRQVLOnmgOB0q.7IY6.k2Bzq', 'EMPLOYEE', 'Charlie Brown', 'charlie.brown@company.com', '+1-555-0303', 'ACTIVE'),
(7, 'emp4', '$2a$12$fGgRGGrFRxsnMEFB3TS.jug5Ik//1xRQVLOnmgOB0q.7IY6.k2Bzq', 'EMPLOYEE', 'Diana Prince', 'diana.prince@company.com', '+1-555-0304', 'ACTIVE'),
(8, 'emp5', '$2a$12$fGgRGGrFRxsnMEFB3TS.jug5Ik//1xRQVLOnmgOB0q.7IY6.k2Bzq', 'EMPLOYEE', 'Ethan Hunt', 'ethan.hunt@company.com', '+1-555-0305', 'ACTIVE'),
(9, 'emp6', '$2a$12$fGgRGGrFRxsnMEFB3TS.jug5Ik//1xRQVLOnmgOB0q.7IY6.k2Bzq', 'EMPLOYEE', 'Fiona Gallagher', 'fiona.gallagher@company.com', '+1-555-0306', 'ACTIVE'),
(10, 'emp7', '$2a$12$fGgRGGrFRxsnMEFB3TS.jug5Ik//1xRQVLOnmgOB0q.7IY6.k2Bzq', 'EMPLOYEE', 'George Clark', 'george.clark@company.com', '+1-555-0307', 'ACTIVE'),
(11, 'emp8', '$2a$12$fGgRGGrFRxsnMEFB3TS.jug5Ik//1xRQVLOnmgOB0q.7IY6.k2Bzq', 'EMPLOYEE', 'Hannah Abbott', 'hannah.abbott@company.com', '+1-555-0308', 'ACTIVE'),
(12, 'emp9', '$2a$12$fGgRGGrFRxsnMEFB3TS.jug5Ik//1xRQVLOnmgOB0q.7IY6.k2Bzq', 'EMPLOYEE', 'Ian Malcolm', 'ian.malcolm@company.com', '+1-555-0309', 'ACTIVE'),
(13, 'emp10', '$2a$12$fGgRGGrFRxsnMEFB3TS.jug5Ik//1xRQVLOnmgOB0q.7IY6.k2Bzq', 'EMPLOYEE', 'Julia Roberts', 'julia.roberts@company.com', '+1-555-0310', 'ACTIVE');

-- 3. Insert Employees mapping
INSERT INTO employees (id, user_id, department_id, manager_id, job_title, hire_date, salary) VALUES
-- Sarah's team (Engineering)
(1, 4, 1, 2, 'Senior Backend Engineer', '2022-03-15', 96000.00),
(2, 5, 1, 2, 'Frontend Developer', '2023-01-10', 82000.00),
(3, 6, 1, 2, 'Full Stack Engineer', '2023-06-01', 88000.00),
(4, 7, 1, 2, 'QA Automation Engineer', '2023-09-15', 78000.00),
(5, 8, 1, 2, 'DevOps & Cloud Engineer', '2022-11-20', 92000.00),

-- David's team (Sales, Marketing & HR)
(6, 9, 2, 3, 'Account Executive', '2022-05-18', 76000.00),
(7, 10, 2, 3, 'Digital Marketing Specialist', '2023-03-01', 71000.00),
(8, 11, 2, 3, 'Sales Development Rep', '2024-02-15', 63000.00),
(9, 12, 2, 3, 'Content Strategist', '2023-07-20', 69000.00),
(10, 13, 3, 3, 'HR Generalist', '2022-08-10', 74000.00);

-- 4. Insert Evaluation Cycles
INSERT INTO evaluation_cycles (id, name, description, start_date, end_date, status) VALUES
(1, 'Annual Performance Review 2025', 'Annual comprehensive employee performance appraisal for FY 2025.', '2025-01-01', '2025-12-31', 'COMPLETED'),
(2, 'Mid-Year Appraisal 2026', 'Mid-year milestone progress and performance appraisal for FY 2026.', '2026-06-01', '2026-07-31', 'ACTIVE');

-- 5. Insert Evaluation Criteria (Weights sum to 100%)
INSERT INTO evaluation_criteria (id, name, description, weight, is_active) VALUES
(1, 'Attendance', 'Reliability, punctuality, attendance record, and adherence to working hours', 15.00, TRUE),
(2, 'Work Quality', 'Thoroughness, technical precision, elegance, and adherence to company quality standards', 20.00, TRUE),
(3, 'Productivity', 'Efficiency, task velocity, volume of output, and meeting deadlines consistently', 20.00, TRUE),
(4, 'Teamwork', 'Cross-functional collaboration, peer mentorship, supporting teammates, and cultural contribution', 15.00, TRUE),
(5, 'Communication', 'Clarity, listening skills, written and verbal responsiveness, stakeholder communication', 15.00, TRUE),
(6, 'Goal Achievement', 'Delivery on agreed quarterly OKRs, milestone completion, and strategic initiative impact', 15.00, TRUE);

-- 6. Insert Cycle 1 Evaluations (Completed FY2025 for all 10 employees)
-- Alice Zhang: 5, 5, 5, 4, 4, 5 -> Weighted: (5*0.15 + 5*0.2 + 5*0.2 + 4*0.15 + 4*0.15 + 5*0.15) = 0.75+1.0+1.0+0.6+0.6+0.75 = 4.70 -> Outstanding
INSERT INTO evaluations (id, employee_id, manager_id, cycle_id, total_score, rating_label, overall_feedback, strengths, improvements, status, submitted_at) VALUES
(1, 1, 2, 1, 4.70, 'Outstanding', 'Alice continues to be a rockstar in backend architecture and API performance.', 'Architectural foresight, high code quality, proactive incident triage.', 'Could take on more public tech talks and external mentoring.', 'APPROVED', '2025-12-20 14:30:00'),
(2, 2, 1, 1, 4.10, 'Exceeds Expectations', 'Bob delivered outstanding UI updates and greatly improved customer-facing app speed.', 'Creative UX design, fast prototyping, reliable turnaround.', 'Deepen automated end-to-end testing skills.', 'APPROVED', '2025-12-21 11:15:00'),
(3, 3, 2, 1, 3.20, 'Meets Expectations', 'Charlie demonstrated steady consistency across full-stack feature delivery.', 'Adaptable across frontend and backend, reliable team player.', 'Increase focus on edge case test coverage and documentation.', 'APPROVED', '2025-12-22 16:45:00'),
(4, 4, 2, 1, 3.90, 'Exceeds Expectations', 'Diana established our automated test suite that reduced regression bugs by 45%.', 'Automation framework design, diligence, high bug catch rate.', 'Speed up initial test scripting for rapid feature releases.', 'APPROVED', '2025-12-22 09:30:00'),
(5, 5, 2, 1, 4.85, 'Outstanding', 'Ethan spearheaded our cloud migration and infrastructure cost reduction with zero downtime.', 'Kubernetes and CI/CD mastery, high availability uptime, security mindset.', 'Continue delegating tier-1 ops tasks to juniors.', 'APPROVED', '2025-12-23 15:20:00'),
(6, 6, 3, 1, 3.40, 'Meets Expectations', 'Fiona achieved 98% of quota and maintained strong client retention.', 'Relationship management, client empathy, pipeline discipline.', 'Accelerate outbound prospecting for enterprise accounts.', 'APPROVED', '2025-12-20 10:10:00'),
(7, 7, 3, 1, 4.05, 'Exceeds Expectations', 'George generated 30% higher inbound leads through optimized campaigns.', 'Data-driven marketing, SEO strategy, creative copy.', 'Better coordination with SDR team on lead qualification criteria.', 'APPROVED', '2025-12-21 13:00:00'),
(8, 8, 3, 1, 2.30, 'Needs Improvement', 'Hannah struggled with qualification quota targets in Q3 and Q4.', 'Enthusiastic attitude, quick learner.', 'Must improve call conversion rate and follow-up rigor.', 'APPROVED', '2025-12-23 17:00:00'),
(9, 9, 3, 1, 3.15, 'Meets Expectations', 'Ian consistently published high quality thought leadership and product guides.', 'Excellent writing tone, content research, storytelling.', 'Improve distribution strategy across social and partner channels.', 'APPROVED', '2025-12-22 14:10:00'),
(10, 10, 3, 1, 4.30, 'Exceeds Expectations', 'Julia significantly enhanced employee onboarding workflows and satisfaction metrics.', 'Empathetic listener, compliance knowledge, streamlined onboarding.', 'Expand data analytics in performance tracking.', 'APPROVED', '2025-12-23 16:30:00');

-- 7. Insert Cycle 1 Scores
-- Eval 1 (Alice Zhang): 5, 5, 5, 4, 4, 5
INSERT INTO evaluation_scores (evaluation_id, criterion_id, score, comments) VALUES
(1, 1, 5, 'Flawless attendance and always punctual to engineering standups.'),
(1, 2, 5, 'Code reviews consistently praise her clean and modular implementations.'),
(1, 3, 5, 'Consistently delivers complex microservices ahead of sprint schedule.'),
(1, 4, 4, 'Very supportive of juniors and actively participates in architectural reviews.'),
(1, 5, 4, 'Articulates technical decisions clearly in PR descriptions and specs.'),
(1, 6, 5, 'Surpassed all annual key performance indicators and OKRs.');

-- Eval 2 (Bob Martin): 4, 4, 4, 4, 4, 4
INSERT INTO evaluation_scores (evaluation_id, criterion_id, score, comments) VALUES
(2, 1, 4, 'Regular attendance with clear communication when taking leave.'),
(2, 2, 4, 'Polished web components with strong attention to accessibility.'),
(2, 3, 4, 'Steady delivery of sprint backlog items.'),
(2, 4, 5, 'Great collaborator with product design and backend teams.'),
(2, 5, 4, 'Clear sprint demo presentations and prompt Slack replies.'),
(2, 6, 4, 'Met all frontend feature delivery targets.');

-- Eval 3 (Charlie Brown): 3, 3, 3, 4, 3, 3
INSERT INTO evaluation_scores (evaluation_id, criterion_id, score, comments) VALUES
(3, 1, 3, 'Satisfactory attendance; occasionally joins meetings right at start.'),
(3, 2, 3, 'Functional code; occasional minor review revisions required.'),
(3, 3, 3, 'Maintains reasonable task velocity.'),
(3, 4, 4, 'Cooperative and always willing to help out during crunch times.'),
(3, 5, 3, 'Communication is satisfactory; needs to update ticket statuses more promptly.'),
(3, 6, 3, 'Delivered main OKR deliverables on time.');

-- Eval 4 (Diana Prince): 4, 4, 4, 4, 3, 4
INSERT INTO evaluation_scores (evaluation_id, criterion_id, score, comments) VALUES
(4, 1, 4, 'Reliable and consistent attendance.'),
(4, 2, 4, 'Comprehensive automated test scripts with high assertion accuracy.'),
(4, 3, 4, 'Rapid turnaround on testing release candidates.'),
(4, 4, 4, 'Active partner with dev team on bug reproduction.'),
(4, 5, 3, 'Bug tickets are clear; could be more vocal during planning sessions.'),
(4, 6, 4, 'Achieved target 90% automation coverage on core user flows.');

-- Eval 5 (Ethan Hunt): 5, 5, 5, 5, 4, 5
INSERT INTO evaluation_scores (evaluation_id, criterion_id, score, comments) VALUES
(5, 1, 5, 'Exceptional on-call availability and zero missed shift rotations.'),
(5, 2, 5, 'Infrastructure as Code is pristine, modular, and well tested.'),
(5, 3, 5, 'Automated deployments reduced release cycle times by 70%.'),
(5, 4, 5, 'Mentors engineers on containerization and cloud security best practices.'),
(5, 5, 4, 'Incident postmortems are thorough and well written.'),
(5, 6, 5, '100% achieved cloud SLA of 99.99% uptime.');

-- Eval 6 (Fiona Gallagher): 4, 3, 3, 4, 4, 3
INSERT INTO evaluation_scores (evaluation_id, criterion_id, score, comments) VALUES
(6, 1, 4, 'Consistently on time for client calls and team syncs.'),
(6, 2, 3, 'Proposals are solid; occasional typos in initial drafts.'),
(6, 3, 3, 'Maintains an active pipeline.'),
(6, 4, 4, 'Shares competitive intelligence with the sales team.'),
(6, 5, 4, 'Charismatic presenter and strong negotiator.'),
(6, 6, 3, 'Met 98% of annual quota target.');

-- Eval 7 (George Clark): 4, 4, 4, 4, 4, 4
INSERT INTO evaluation_scores (evaluation_id, criterion_id, score, comments) VALUES
(7, 1, 4, 'Punctual and reliable.'),
(7, 2, 4, 'High ROI on digital ad spend and campaign creative.'),
(7, 3, 4, 'Manages multiple advertising channels smoothly.'),
(7, 4, 4, 'Collaborates well with sales to optimize lead attribution.'),
(7, 5, 4, 'Presents campaign analytics clearly to leadership.'),
(7, 6, 4, 'Exceeded annual MQL goal by 15%.');

-- Eval 8 (Hannah Abbott): 2, 2, 2, 3, 3, 2
INSERT INTO evaluation_scores (evaluation_id, criterion_id, score, comments) VALUES
(8, 1, 3, 'Punctuality is fine; needs to be more structured with schedule.'),
(8, 2, 2, 'Lead data entry often has missing fields in CRM.'),
(8, 3, 2, 'Call and email outbound volume is 25% below expectation.'),
(8, 4, 3, 'Willing to learn from senior account reps.'),
(8, 5, 3, 'Good spoken communication, but needs deeper objection handling practice.'),
(8, 6, 2, 'Did not hit SDR demo quota in the second half of the year.');

-- Eval 9 (Ian Malcolm): 3, 4, 3, 3, 3, 3
INSERT INTO evaluation_scores (evaluation_id, criterion_id, score, comments) VALUES
(9, 1, 3, 'Standard attendance.'),
(9, 2, 4, 'In-depth research and highly engaging whitepapers.'),
(9, 3, 3, 'Content publication pace could be accelerated.'),
(9, 4, 3, 'Cooperates with product marketing on asset needs.'),
(9, 5, 3, 'Great writing; needs more active collaboration in verbal meetings.'),
(9, 6, 3, 'Delivered core content roadmap pieces.');

-- Eval 10 (Julia Roberts): 5, 4, 4, 4, 5, 4
INSERT INTO evaluation_scores (evaluation_id, criterion_id, score, comments) VALUES
(10, 1, 5, 'Always available and responds promptly to employee inquiries.'),
(10, 2, 4, 'Employee records and HR policies managed meticulously.'),
(10, 3, 4, 'Handles complex employee relations cases swiftly.'),
(10, 4, 4, 'Trusted liaison across all departments.'),
(10, 5, 5, 'Exceptional diplomatic communication and empathy.'),
(10, 6, 4, 'Implemented new benefits portal on time and within budget.');

-- 8. Insert Cycle 2 Evaluations (Active Mid-Year 2026)
-- 5 Completed/Submitted Evaluations
INSERT INTO evaluations (id, employee_id, manager_id, cycle_id, total_score, rating_label, overall_feedback, strengths, improvements, status, submitted_at) VALUES
(11, 1, 2, 2, 4.85, 'Outstanding', 'Alice continues her phenomenal leadership as lead backend engineer on our microservices overhaul.', 'Deep distributed system expertise, exceptional execution speed.', 'Share learnings via tech blog post.', 'APPROVED', '2026-06-25 10:00:00'),
(12, 2, 2, 2, 4.30, 'Exceeds Expectations', 'Bob delivered the entire responsive dashboard redesign with great style and speed.', 'Pixel perfect implementation, fast load performance.', 'Expand unit tests for state stores.', 'APPROVED', '2026-06-26 14:20:00'),
(13, 6, 3, 2, 3.80, 'Exceeds Expectations', 'Fiona opened two major enterprise accounts this quarter.', 'Strong executive presence and deal structuring.', 'Maintain consistency across mid-market tier.', 'APPROVED', '2026-06-27 11:30:00'),
(14, 7, 3, 2, 4.20, 'Exceeds Expectations', 'George revamped our search ads and doubled CTR.', 'Analytical rigor, conversion optimization.', 'Work closer with product team on launch announcements.', 'APPROVED', '2026-06-28 15:45:00'),
(15, 10, 3, 2, 4.50, 'Outstanding', 'Julia orchestrated the mid-year performance cycle rollout flawlessly.', 'Great organization, high trust from staff, proactive guidance.', 'Automate reminder notifications.', 'APPROVED', '2026-06-29 09:15:00');

-- Cycle 2 Scores for the 5 submitted evaluations:
-- Eval 11 (Alice)
INSERT INTO evaluation_scores (evaluation_id, criterion_id, score, comments) VALUES
(11, 1, 5, 'Always dependable.'),
(11, 2, 5, 'Architectural patterns are stellar.'),
(11, 3, 5, 'Delivered payments engine rewrite ahead of time.'),
(11, 4, 5, 'Superb technical leadership.'),
(11, 5, 4, 'Clear design documentation.'),
(11, 6, 5, 'All mid-year milestones achieved.');

-- Eval 12 (Bob)
INSERT INTO evaluation_scores (evaluation_id, criterion_id, score, comments) VALUES
(12, 1, 4, 'Reliable work presence.'),
(12, 2, 5, 'UI aesthetics and accessibility are top tier.'),
(12, 3, 4, 'Fast component turnaround.'),
(12, 4, 4, 'Good cross-functional teamwork.'),
(12, 5, 4, 'Engaging sprint demos.'),
(12, 6, 4, 'Frontend roadmap is on track.');

-- Eval 13 (Fiona)
INSERT INTO evaluation_scores (evaluation_id, criterion_id, score, comments) VALUES
(13, 1, 4, 'Punctual with clients.'),
(13, 2, 4, 'Quality enterprise pitch decks.'),
(13, 3, 4, 'Solid sales activity volume.'),
(13, 4, 3, 'Good team player.'),
(13, 5, 4, 'Effective communicator with stakeholders.'),
(13, 6, 4, 'Closed 110% of mid-year quota.');

-- Eval 14 (George)
INSERT INTO evaluation_scores (evaluation_id, criterion_id, score, comments) VALUES
(14, 1, 4, 'Consistent attendance.'),
(14, 2, 4, 'Creative and analytical campaign execution.'),
(14, 3, 4, 'High velocity output across paid channels.'),
(14, 4, 4, 'Good synergy with sales teams.'),
(14, 5, 4, 'Actionable reporting.'),
(14, 6, 5, 'Exceeded lead generation mid-year goal by 25%.');

-- Eval 15 (Julia)
INSERT INTO evaluation_scores (evaluation_id, criterion_id, score, comments) VALUES
(15, 1, 5, 'Always prompt and approachable.'),
(15, 2, 4, 'Meticulous policy execution.'),
(15, 3, 5, 'Handled appraisal system rollout seamlessly.'),
(15, 4, 5, 'Great team mediator.'),
(15, 5, 5, 'Empathetic and clear communicator.'),
(15, 6, 4, 'Delivered HR goals for the half-year.');

-- Pending Evaluations in Cycle 2 (Employees 3, 4, 5 under Sarah; Employees 8, 9 under David)
-- These are in DRAFT status or not yet created, allowing Managers to evaluate them in real time!
INSERT INTO evaluations (id, employee_id, manager_id, cycle_id, total_score, rating_label, overall_feedback, strengths, improvements, status, submitted_at) VALUES
(16, 3, 2, 2, NULL, NULL, 'Draft evaluation in progress...', 'Good work on API endpoints.', 'Needs to review test coverage.', 'DRAFT', NULL),
(17, 8, 3, 2, NULL, NULL, 'Initial mid-year check-in notes recorded.', 'Improving call volume.', 'Focus on converting high intent leads.', 'DRAFT', NULL);
-- Employees 4 (Diana), 5 (Ethan), and 9 (Ian) currently have NO evaluation in Cycle 2,
-- so the manager can click "Start Evaluation" directly from their team list!
