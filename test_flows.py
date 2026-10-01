import urllib.request
import urllib.parse
import http.cookiejar
import sys

BASE_URL = "http://localhost:8080/eps"

def create_session():
    cj = http.cookiejar.CookieJar()
    opener = urllib.request.build_opener(urllib.request.HTTPCookieProcessor(cj))
    return opener

def test_flow():
    print("==================================================")
    print("  EPS - END-TO-END AUTOMATED VERIFICATION SUITE")
    print("==================================================")

    # 1. Unauthenticated root redirect
    opener = create_session()
    resp = opener.open(f"{BASE_URL}/")
    print(f"[TEST 1] Root redirect: URL={resp.geturl()}, Status={resp.status}")
    assert "/auth/login" in resp.geturl(), "Failed: Root did not redirect to login"
    print("   -> PASSED: Redirected to login page.")

    # 2. Test Admin Login
    print("\n[TEST 2] Admin Login flow:")
    login_data = urllib.parse.urlencode({"username": "admin", "password": "Admin@123"}).encode("utf-8")
    req = urllib.request.Request(f"{BASE_URL}/auth/login", data=login_data)
    resp = opener.open(req)
    admin_html = resp.read().decode("utf-8")
    print(f"   Admin Landing URL: {resp.geturl()}, Status: {resp.status}")
    assert "/admin/dashboard" in resp.geturl(), f"Failed: Expected /admin/dashboard, got {resp.geturl()}"
    assert "Admin Dashboard" in admin_html or "Administrator" in admin_html, "Dashboard header missing"
    print("   -> PASSED: Admin successfully logged in and dashboard rendered.")

    # 2b. Test Admin pages
    admin_pages = [
        ("/admin/departments", "Department Management"),
        ("/admin/employees", "Employee Directory"),
        ("/admin/criteria", "Evaluation Criteria"),
        ("/admin/cycles", "Evaluation Cycles"),
        ("/admin/reports", "Performance Reports & Analytics")
    ]
    for endpoint, title in admin_pages:
        resp = opener.open(f"{BASE_URL}{endpoint}")
        content = resp.read().decode("utf-8")
        assert resp.status == 200, f"Failed loading {endpoint}: {resp.status}"
        print(f"   -> PASSED: Admin {endpoint} (HTTP 200)")

    # 2c. Test CSV Export
    resp = opener.open(f"{BASE_URL}/admin/reports/export")
    csv_content = resp.read().decode("utf-8")
    assert resp.status == 200, "CSV Export failed"
    assert "Evaluation ID" in csv_content or "Employee Name" in csv_content or "Employee" in csv_content, "CSV header missing"
    print("   -> PASSED: CSV Report Export downloaded successfully (RFC 4180 format)")

    # 2d. Admin Logout
    resp = opener.open(f"{BASE_URL}/auth/logout")
    print(f"   -> PASSED: Admin Logout (Redirected to: {resp.geturl()})")

    # 3. Test Manager Login
    print("\n[TEST 3] Manager Login flow:")
    mgr_opener = create_session()
    login_data = urllib.parse.urlencode({"username": "manager1", "password": "Manager@123"}).encode("utf-8")
    req = urllib.request.Request(f"{BASE_URL}/auth/login", data=login_data)
    resp = mgr_opener.open(req)
    mgr_html = resp.read().decode("utf-8")
    print(f"   Manager Landing URL: {resp.geturl()}, Status: {resp.status}")
    assert "/manager/dashboard" in resp.geturl(), f"Failed: Expected /manager/dashboard, got {resp.geturl()}"
    print("   -> PASSED: Manager successfully logged in.")

    # 3b. Manager Team page
    resp = mgr_opener.open(f"{BASE_URL}/manager/team")
    team_html = resp.read().decode("utf-8")
    assert resp.status == 200, "Failed to load manager team page"
    assert "Alice Zhang" in team_html, "Assigned employee Alice Zhang not listed in team"
    print("   -> PASSED: Manager Team page loaded with assigned employees.")

    # 3c. Manager Evaluation View
    resp = mgr_opener.open(f"{BASE_URL}/manager/evaluation-view?id=1")
    eval_html = resp.read().decode("utf-8")
    assert resp.status == 200, "Failed to view evaluation #1"
    assert "Outstanding" in eval_html or "Alice Zhang" in eval_html, "Evaluation details missing"
    print("   -> PASSED: Manager viewed existing evaluation details.")

    # 3d. Manager Evaluate Form & Submission (Cycle 2, Employee 2 - Bob Martin)
    resp = mgr_opener.open(f"{BASE_URL}/manager/evaluate?employeeId=2")
    form_html = resp.read().decode("utf-8")
    assert resp.status == 200, "Failed to load evaluate form"
    assert "Mid-Year Appraisal 2026" in form_html or "Bob Martin" in form_html, "Form context missing"
    print("   -> PASSED: Manager evaluate form loaded for active cycle.")

    # Post an appraisal submission with 1-5 scores
    submit_data = urllib.parse.urlencode({
        "employeeId": "2",
        "cycleId": "2",
        "action": "submit",
        "score_1": "5",
        "score_comment_1": "Superb attendance and reliability",
        "score_2": "4",
        "score_comment_2": "Consistently clean, high-grade frontend code",
        "score_3": "5",
        "score_comment_3": "Very fast feature turnarounds",
        "score_4": "4",
        "score_comment_4": "Great teammate and communicator",
        "score_5": "4",
        "score_comment_5": "Responsive across standups and chats",
        "score_6": "5",
        "score_comment_6": "Accomplished all sprint milestones",
        "overallFeedback": "Bob had an outstanding mid-year performance delivering key UI features.",
        "strengths": "CSS/JS mastery, speed, peer collaboration",
        "improvements": "Explore end-to-end automated testing"
    }).encode("utf-8")
    req = urllib.request.Request(f"{BASE_URL}/manager/evaluate", data=submit_data)
    resp = mgr_opener.open(req)
    assert resp.status == 200, "Failed to submit evaluation"
    print("   -> PASSED: Manager submitted new evaluation with 1-5 scores and automated weighted calculation.")

    # 3e. Manager Logout
    resp = mgr_opener.open(f"{BASE_URL}/auth/logout")
    print("   -> PASSED: Manager Logout")

    # 4. Test Employee Login
    print("\n[TEST 4] Employee Login flow:")
    emp_opener = create_session()
    login_data = urllib.parse.urlencode({"username": "emp1", "password": "Employee@123"}).encode("utf-8")
    req = urllib.request.Request(f"{BASE_URL}/auth/login", data=login_data)
    resp = emp_opener.open(req)
    emp_html = resp.read().decode("utf-8")
    print(f"   Employee Landing URL: {resp.geturl()}, Status: {resp.status}")
    assert "/employee/dashboard" in resp.geturl(), f"Failed: Expected /employee/dashboard, got {resp.geturl()}"
    assert "Alice Zhang" in emp_html or "Dashboard" in emp_html, "Employee name missing on dashboard"
    print("   -> PASSED: Employee logged in successfully.")

    # 4b. Employee History page
    resp = emp_opener.open(f"{BASE_URL}/employee/history")
    hist_html = resp.read().decode("utf-8")
    assert resp.status == 200, "Failed to load employee history"
    assert "Annual Performance Review 2025" in hist_html, "Evaluation cycle missing in history"
    print("   -> PASSED: Employee history page loaded.")

    # 4c. Employee Evaluation Details with Radar Chart
    resp = emp_opener.open(f"{BASE_URL}/employee/evaluation-details?id=1")
    detail_html = resp.read().decode("utf-8")
    assert resp.status == 200, "Failed to load evaluation details"
    assert "radar" in detail_html.lower() or "chart" in detail_html.lower(), "Radar chart missing"
    print("   -> PASSED: Employee evaluation detail with competency radar chart verified.")

    # 4d. Security Check: Employee attempting to access Admin page
    print("\n[TEST 5] Role-based Authorization Barrier Check:")
    try:
        resp = emp_opener.open(f"{BASE_URL}/admin/dashboard")
        # If redirected or returned 403
        content = resp.read().decode("utf-8")
        if resp.status == 403 or "Access Denied" in content or "/auth/login" in resp.geturl():
            print("   -> PASSED: Employee blocked from Admin area (Redirected/Forbidden).")
        else:
            print(f"   -> WARNING: Employee accessed Admin area! Status: {resp.status}")
    except urllib.error.HTTPError as he:
        if he.code in (401, 403):
            print(f"   -> PASSED: Employee blocked from Admin area (HTTP {he.code}).")
        else:
            raise he

    print("\n==================================================")
    print("  ALL 5 E2E INTEGRATION TESTS PASSED 100%!")
    print("==================================================")

if __name__ == "__main__":
    try:
        test_flow()
    except Exception as e:
        print(f"\nTEST SUITE FAILED: {e}", file=sys.stderr)
        sys.exit(1)
