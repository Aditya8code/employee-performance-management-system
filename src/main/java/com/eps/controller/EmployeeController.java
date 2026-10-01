package com.eps.controller;

import com.eps.model.Employee;
import com.eps.model.Evaluation;
import com.eps.model.EvaluationCycle;
import com.eps.model.User;
import com.eps.service.EmployeeService;
import com.eps.service.EvaluationCycleService;
import com.eps.service.EvaluationService;
import com.eps.service.impl.EmployeeServiceImpl;
import com.eps.service.impl.EvaluationCycleServiceImpl;
import com.eps.service.impl.EvaluationServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller handling Employee self-service features:
 * - Personal dashboard, performance score & rating badge
 * - Evaluation history across cycles
 * - Detailed evaluation breakdown with individual criterion scores, manager feedback, and Chart.js radar charts
 */
@WebServlet(name = "EmployeeController", urlPatterns = {
        "/employee/dashboard",
        "/employee/history",
        "/employee/evaluation-details"
})
public class EmployeeController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final EmployeeService employeeService = new EmployeeServiceImpl();
    private final EvaluationCycleService cycleService = new EvaluationCycleServiceImpl();
    private final EvaluationService evaluationService = new EvaluationServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/employee/dashboard":
                showDashboard(request, response);
                break;
            case "/employee/history":
                showHistory(request, response);
                break;
            case "/employee/evaluation-details":
                showEvaluationDetails(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/employee/dashboard");
                break;
        }
    }

    private Employee getCurrentEmployee(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        User user = (User) session.getAttribute("currentUser");
        if (user == null) return null;
        try {
            return employeeService.getEmployeeByUserId(user.getId());
        } catch (Exception e) {
            return null;
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Employee employee = getCurrentEmployee(request);
        if (employee == null) {
            request.getSession().setAttribute("flashError", "Employee profile not found.");
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        List<Evaluation> history = evaluationService.getEmployeeEvaluations(employee.getEmployeeId());
        EvaluationCycle activeCycle = cycleService.getActiveCycle();

        Evaluation currentCycleEval = null;
        Evaluation latestCompletedEval = null;

        for (Evaluation ev : history) {
            if (activeCycle != null && ev.getCycleId().equals(activeCycle.getId())) {
                currentCycleEval = ev;
            }
            if (ev.isSubmitted() && latestCompletedEval == null) {
                latestCompletedEval = ev;
            }
        }

        request.setAttribute("employee", employee);
        request.setAttribute("history", history);
        request.setAttribute("activeCycle", activeCycle);
        request.setAttribute("currentCycleEval", currentCycleEval);
        request.setAttribute("latestCompletedEval", latestCompletedEval);

        request.getRequestDispatcher("/WEB-INF/views/employee/dashboard.jsp").forward(request, response);
    }

    private void showHistory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Employee employee = getCurrentEmployee(request);
        if (employee == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        List<Evaluation> history = evaluationService.getEmployeeEvaluations(employee.getEmployeeId());
        request.setAttribute("employee", employee);
        request.setAttribute("history", history);

        request.getRequestDispatcher("/WEB-INF/views/employee/history.jsp").forward(request, response);
    }

    private void showEvaluationDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Employee employee = getCurrentEmployee(request);
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/employee/history");
            return;
        }

        int evalId = Integer.parseInt(idParam);
        Evaluation evaluation = evaluationService.getEvaluationById(evalId);

        // Security check: verify this evaluation belongs to the logged-in employee (or Admin)
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser != null && !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            if (employee == null || !evaluation.getEmployeeId().equals(employee.getEmployeeId())) {
                request.getSession().setAttribute("flashError", "Access denied: You can only view your own evaluation records.");
                response.sendRedirect(request.getContextPath() + "/employee/history");
                return;
            }
        }

        request.setAttribute("evaluation", evaluation);
        request.setAttribute("employee", employee);

        request.getRequestDispatcher("/WEB-INF/views/employee/evaluation-details.jsp").forward(request, response);
    }
}
