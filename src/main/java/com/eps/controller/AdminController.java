package com.eps.controller;

import com.eps.exception.ValidationException;
import com.eps.model.Department;
import com.eps.model.DepartmentStats;
import com.eps.model.Employee;
import com.eps.model.Evaluation;
import com.eps.model.EvaluationCriterion;
import com.eps.model.EvaluationCycle;
import com.eps.model.ReportFilter;
import com.eps.model.User;
import com.eps.service.DepartmentService;
import com.eps.service.EmployeeService;
import com.eps.service.EvaluationCriterionService;
import com.eps.service.EvaluationCycleService;
import com.eps.service.ReportService;
import com.eps.service.impl.DepartmentServiceImpl;
import com.eps.service.impl.EmployeeServiceImpl;
import com.eps.service.impl.EvaluationCriterionServiceImpl;
import com.eps.service.impl.EvaluationCycleServiceImpl;
import com.eps.service.impl.ReportServiceImpl;
import com.eps.util.DateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller managing all Administrative responsibilities:
 * - Dashboard KPIs & cycle progress
 * - Departments CRUD
 * - Employees CRUD & manager assignment
 * - Evaluation Criteria management & weightage
 * - Evaluation Cycles lifecycle
 * - Organization Performance Reports & CSV export
 */
@WebServlet(name = "AdminController", urlPatterns = {
        "/admin/dashboard",
        "/admin/departments",
        "/admin/employees",
        "/admin/criteria",
        "/admin/cycles",
        "/admin/reports",
        "/admin/reports/export"
})
public class AdminController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final DepartmentService departmentService = new DepartmentServiceImpl();
    private final EmployeeService employeeService = new EmployeeServiceImpl();
    private final EvaluationCriterionService criterionService = new EvaluationCriterionServiceImpl();
    private final EvaluationCycleService cycleService = new EvaluationCycleServiceImpl();
    private final ReportService reportService = new ReportServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/admin/dashboard":
                showDashboard(request, response);
                break;
            case "/admin/departments":
                showDepartments(request, response);
                break;
            case "/admin/employees":
                showEmployees(request, response);
                break;
            case "/admin/criteria":
                showCriteria(request, response);
                break;
            case "/admin/cycles":
                showCycles(request, response);
                break;
            case "/admin/reports":
                showReports(request, response);
                break;
            case "/admin/reports/export":
                exportReportsCSV(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/admin/departments":
                handleDepartmentPost(request, response);
                break;
            case "/admin/employees":
                handleEmployeePost(request, response);
                break;
            case "/admin/criteria":
                handleCriteriaPost(request, response);
                break;
            case "/admin/cycles":
                handleCyclePost(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                break;
        }
    }

    // --- View Renderers ---

    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> dashboardStats = reportService.getAdminDashboardStats();
        request.setAttribute("stats", dashboardStats);

        List<EvaluationCycle> cycles = cycleService.getAllCycles();
        request.setAttribute("cycles", cycles);

        EvaluationCycle activeCycle = (EvaluationCycle) dashboardStats.get("activeCycle");
        if (activeCycle != null) {
            List<DepartmentStats> deptStats = reportService.getDepartmentStats(activeCycle.getId());
            request.setAttribute("deptStats", deptStats);
        }

        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }

    private void showDepartments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Department> departments = departmentService.getAllDepartments();
        request.setAttribute("departments", departments);
        request.getRequestDispatcher("/WEB-INF/views/admin/departments.jsp").forward(request, response);
    }

    private void showEmployees(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Employee> employees = employeeService.getAllEmployees();
        List<Department> departments = departmentService.getAllDepartments();
        List<User> managers = employeeService.getAllManagers();

        request.setAttribute("employees", employees);
        request.setAttribute("departments", departments);
        request.setAttribute("managers", managers);

        request.getRequestDispatcher("/WEB-INF/views/admin/employees.jsp").forward(request, response);
    }

    private void showCriteria(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<EvaluationCriterion> criteria = criterionService.getAllCriteria();
        BigDecimal totalWeight = BigDecimal.ZERO;
        for (EvaluationCriterion ec : criteria) {
            if (ec.isActive() && ec.getWeight() != null) {
                totalWeight = totalWeight.add(ec.getWeight());
            }
        }
        request.setAttribute("criteria", criteria);
        request.setAttribute("totalWeight", totalWeight);
        request.getRequestDispatcher("/WEB-INF/views/admin/criteria.jsp").forward(request, response);
    }

    private void showCycles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<EvaluationCycle> cycles = cycleService.getAllCycles();
        request.setAttribute("cycles", cycles);
        request.getRequestDispatcher("/WEB-INF/views/admin/cycles.jsp").forward(request, response);
    }

    private void showReports(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<EvaluationCycle> cycles = cycleService.getAllCycles();
        List<Department> departments = departmentService.getAllDepartments();

        String cycleIdParam = request.getParameter("cycleId");
        String deptIdParam = request.getParameter("departmentId");
        String ratingParam = request.getParameter("ratingLabel");
        String statusParam = request.getParameter("status");

        Integer cycleId = null;
        if (cycleIdParam != null && !cycleIdParam.trim().isEmpty()) {
            try {
                cycleId = Integer.parseInt(cycleIdParam);
            } catch (NumberFormatException ignored) {
            }
        } else {
            EvaluationCycle active = cycleService.getActiveCycle();
            if (active != null) {
                cycleId = active.getId();
            } else if (!cycles.isEmpty()) {
                cycleId = cycles.get(0).getId();
            }
        }

        Integer deptId = null;
        if (deptIdParam != null && !deptIdParam.trim().isEmpty()) {
            try {
                deptId = Integer.parseInt(deptIdParam);
            } catch (NumberFormatException ignored) {
            }
        }

        ReportFilter filter = new ReportFilter(cycleId, deptId, ratingParam, statusParam);
        List<Evaluation> evaluations = reportService.getFilteredEvaluations(filter);
        List<DepartmentStats> deptStats = (cycleId != null) ? reportService.getDepartmentStats(cycleId) : null;

        request.setAttribute("cycles", cycles);
        request.setAttribute("departments", departments);
        request.setAttribute("selectedCycleId", cycleId);
        request.setAttribute("selectedDeptId", deptId);
        request.setAttribute("selectedRating", ratingParam);
        request.setAttribute("selectedStatus", statusParam);
        request.setAttribute("evaluations", evaluations);
        request.setAttribute("deptStats", deptStats);

        request.getRequestDispatcher("/WEB-INF/views/admin/reports.jsp").forward(request, response);
    }

    private void exportReportsCSV(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String cycleIdParam = request.getParameter("cycleId");
        String deptIdParam = request.getParameter("departmentId");
        String ratingParam = request.getParameter("ratingLabel");
        String statusParam = request.getParameter("status");

        Integer cycleId = (cycleIdParam != null && !cycleIdParam.trim().isEmpty()) ? Integer.parseInt(cycleIdParam) : null;
        Integer deptId = (deptIdParam != null && !deptIdParam.trim().isEmpty()) ? Integer.parseInt(deptIdParam) : null;

        ReportFilter filter = new ReportFilter(cycleId, deptId, ratingParam, statusParam);

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"performance_report_" + System.currentTimeMillis() + ".csv\"");

        reportService.exportReportCSV(filter, response.getWriter());
    }

    // --- Action Handlers ---

    private void handleDepartmentPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        try {
            if ("create".equalsIgnoreCase(action)) {
                String name = request.getParameter("name");
                String desc = request.getParameter("description");
                departmentService.createDepartment(name, desc);
                request.getSession().setAttribute("flashSuccess", "Department created successfully.");
            } else if ("update".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String desc = request.getParameter("description");
                departmentService.updateDepartment(id, name, desc);
                request.getSession().setAttribute("flashSuccess", "Department updated successfully.");
            } else if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                departmentService.deleteDepartment(id);
                request.getSession().setAttribute("flashSuccess", "Department deleted successfully.");
            }
        } catch (ValidationException ve) {
            request.getSession().setAttribute("flashError", ve.getMessage());
        } catch (Exception ex) {
            request.getSession().setAttribute("flashError", "Failed to process department action: " + ex.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/admin/departments");
    }

    private void handleEmployeePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        try {
            if ("create".equalsIgnoreCase(action)) {
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                Integer deptId = Integer.parseInt(request.getParameter("departmentId"));

                String mgrParam = request.getParameter("managerId");
                Integer mgrId = (mgrParam != null && !mgrParam.trim().isEmpty() && !"-1".equals(mgrParam)) ? Integer.parseInt(mgrParam) : null;

                String jobTitle = request.getParameter("jobTitle");
                Date hireDate = DateUtil.parseSqlDate(request.getParameter("hireDate"));

                String salParam = request.getParameter("salary");
                BigDecimal salary = (salParam != null && !salParam.trim().isEmpty()) ? new BigDecimal(salParam) : BigDecimal.ZERO;

                employeeService.createEmployee(username, password, fullName, email, phone, deptId, mgrId, jobTitle, hireDate, salary);
                request.getSession().setAttribute("flashSuccess", "Employee " + fullName + " registered successfully.");

            } else if ("update".equalsIgnoreCase(action)) {
                int empId = Integer.parseInt(request.getParameter("employeeId"));
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                Integer deptId = Integer.parseInt(request.getParameter("departmentId"));

                String mgrParam = request.getParameter("managerId");
                Integer mgrId = (mgrParam != null && !mgrParam.trim().isEmpty() && !"-1".equals(mgrParam)) ? Integer.parseInt(mgrParam) : null;

                String jobTitle = request.getParameter("jobTitle");
                Date hireDate = DateUtil.parseSqlDate(request.getParameter("hireDate"));

                String salParam = request.getParameter("salary");
                BigDecimal salary = (salParam != null && !salParam.trim().isEmpty()) ? new BigDecimal(salParam) : BigDecimal.ZERO;
                String status = request.getParameter("status");

                employeeService.updateEmployee(empId, fullName, email, phone, deptId, mgrId, jobTitle, hireDate, salary, status);
                request.getSession().setAttribute("flashSuccess", "Employee details updated successfully.");

            } else if ("assign_manager".equalsIgnoreCase(action)) {
                int empId = Integer.parseInt(request.getParameter("employeeId"));
                String mgrParam = request.getParameter("managerId");
                Integer mgrId = (mgrParam != null && !mgrParam.trim().isEmpty() && !"-1".equals(mgrParam)) ? Integer.parseInt(mgrParam) : null;

                employeeService.assignManager(empId, mgrId);
                request.getSession().setAttribute("flashSuccess", "Manager assigned successfully.");

            } else if ("delete".equalsIgnoreCase(action)) {
                int empId = Integer.parseInt(request.getParameter("employeeId"));
                employeeService.deleteEmployee(empId);
                request.getSession().setAttribute("flashSuccess", "Employee record and account deleted.");
            }
        } catch (ValidationException ve) {
            request.getSession().setAttribute("flashError", ve.getMessage());
        } catch (Exception ex) {
            request.getSession().setAttribute("flashError", "Failed to process employee action: " + ex.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/admin/employees");
    }

    private void handleCriteriaPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        try {
            if ("create".equalsIgnoreCase(action)) {
                String name = request.getParameter("name");
                String desc = request.getParameter("description");
                BigDecimal weight = new BigDecimal(request.getParameter("weight"));
                criterionService.createCriterion(name, desc, weight);
                request.getSession().setAttribute("flashSuccess", "Evaluation criterion created.");
            } else if ("update".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String desc = request.getParameter("description");
                BigDecimal weight = new BigDecimal(request.getParameter("weight"));
                boolean active = "true".equalsIgnoreCase(request.getParameter("active")) || "on".equalsIgnoreCase(request.getParameter("active"));
                criterionService.updateCriterion(id, name, desc, weight, active);
                request.getSession().setAttribute("flashSuccess", "Evaluation criterion updated.");
            } else if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                criterionService.deleteCriterion(id);
                request.getSession().setAttribute("flashSuccess", "Evaluation criterion removed.");
            }
        } catch (ValidationException ve) {
            request.getSession().setAttribute("flashError", ve.getMessage());
        } catch (Exception ex) {
            request.getSession().setAttribute("flashError", "Failed to process criteria action: " + ex.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/admin/criteria");
    }

    private void handleCyclePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        try {
            if ("create".equalsIgnoreCase(action)) {
                String name = request.getParameter("name");
                String desc = request.getParameter("description");
                Date startDate = DateUtil.parseSqlDate(request.getParameter("startDate"));
                Date endDate = DateUtil.parseSqlDate(request.getParameter("endDate"));
                cycleService.createCycle(name, desc, startDate, endDate);
                request.getSession().setAttribute("flashSuccess", "Evaluation cycle created as DRAFT.");
            } else if ("update".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String desc = request.getParameter("description");
                Date startDate = DateUtil.parseSqlDate(request.getParameter("startDate"));
                Date endDate = DateUtil.parseSqlDate(request.getParameter("endDate"));
                String status = request.getParameter("status");
                cycleService.updateCycle(id, name, desc, startDate, endDate, status);
                request.getSession().setAttribute("flashSuccess", "Evaluation cycle updated.");
            } else if ("status".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String status = request.getParameter("status");
                cycleService.changeCycleStatus(id, status);
                request.getSession().setAttribute("flashSuccess", "Cycle status transitioned to " + status + ".");
            } else if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                cycleService.deleteCycle(id);
                request.getSession().setAttribute("flashSuccess", "Evaluation cycle deleted.");
            }
        } catch (ValidationException ve) {
            request.getSession().setAttribute("flashError", ve.getMessage());
        } catch (Exception ex) {
            request.getSession().setAttribute("flashError", "Failed to process cycle action: " + ex.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/admin/cycles");
    }
}
