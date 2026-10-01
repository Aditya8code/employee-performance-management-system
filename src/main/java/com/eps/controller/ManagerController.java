package com.eps.controller;

import com.eps.exception.ValidationException;
import com.eps.model.Employee;
import com.eps.model.Evaluation;
import com.eps.model.EvaluationCriterion;
import com.eps.model.EvaluationCycle;
import com.eps.model.EvaluationScore;
import com.eps.model.User;
import com.eps.service.EmployeeService;
import com.eps.service.EvaluationCriterionService;
import com.eps.service.EvaluationCycleService;
import com.eps.service.EvaluationService;
import com.eps.service.impl.EmployeeServiceImpl;
import com.eps.service.impl.EvaluationCriterionServiceImpl;
import com.eps.service.impl.EvaluationCycleServiceImpl;
import com.eps.service.impl.EvaluationServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller handling Manager functions:
 * - Team roster overview
 * - Appraisal evaluation submission & editing during active cycles
 * - Individual criterion scoring (1-5), feedback, strengths, and improvement suggestions
 */
@WebServlet(name = "ManagerController", urlPatterns = {
        "/manager/dashboard",
        "/manager/team",
        "/manager/evaluate",
        "/manager/evaluation-view"
})
public class ManagerController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final EmployeeService employeeService = new EmployeeServiceImpl();
    private final EvaluationCycleService cycleService = new EvaluationCycleServiceImpl();
    private final EvaluationCriterionService criterionService = new EvaluationCriterionServiceImpl();
    private final EvaluationService evaluationService = new EvaluationServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/manager/dashboard":
                showDashboard(request, response);
                break;
            case "/manager/team":
                showTeam(request, response);
                break;
            case "/manager/evaluate":
                showEvaluateForm(request, response);
                break;
            case "/manager/evaluation-view":
                showEvaluationView(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/manager/dashboard");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/manager/evaluate".equals(path)) {
            handleEvaluatePost(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/manager/dashboard");
        }
    }

    private User getCurrentManager(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (User) session.getAttribute("currentUser") : null;
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User manager = getCurrentManager(request);
        List<Employee> team = employeeService.getEmployeesByManager(manager.getId());
        EvaluationCycle activeCycle = cycleService.getActiveCycle();

        int completedCount = 0;
        int pendingCount = 0;

        if (activeCycle != null && team != null) {
            for (Employee emp : team) {
                Evaluation ev = evaluationService.getEvaluationForEmployeeAndCycle(emp.getEmployeeId(), activeCycle.getId());
                if (ev != null && ev.isSubmitted()) {
                    completedCount++;
                } else {
                    pendingCount++;
                }
            }
        }

        request.setAttribute("team", team);
        request.setAttribute("activeCycle", activeCycle);
        request.setAttribute("completedCount", completedCount);
        request.setAttribute("pendingCount", pendingCount);

        request.getRequestDispatcher("/WEB-INF/views/manager/dashboard.jsp").forward(request, response);
    }

    private void showTeam(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User manager = getCurrentManager(request);
        List<Employee> team = employeeService.getEmployeesByManager(manager.getId());
        EvaluationCycle activeCycle = cycleService.getActiveCycle();

        // Map evaluation status per employee for the active cycle
        Map<Integer, Evaluation> evalMap = new HashMap<>();
        if (activeCycle != null && team != null) {
            for (Employee emp : team) {
                Evaluation ev = evaluationService.getEvaluationForEmployeeAndCycle(emp.getEmployeeId(), activeCycle.getId());
                if (ev != null) {
                    evalMap.put(emp.getEmployeeId(), ev);
                }
            }
        }

        request.setAttribute("team", team);
        request.setAttribute("activeCycle", activeCycle);
        request.setAttribute("evalMap", evalMap);

        request.getRequestDispatcher("/WEB-INF/views/manager/team.jsp").forward(request, response);
    }

    private void showEvaluateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User manager = getCurrentManager(request);
        String empIdParam = request.getParameter("employeeId");
        if (empIdParam == null || empIdParam.trim().isEmpty()) {
            request.getSession().setAttribute("flashError", "Employee must be selected to start evaluation.");
            response.sendRedirect(request.getContextPath() + "/manager/team");
            return;
        }

        int empId = Integer.parseInt(empIdParam);
        Employee employee = employeeService.getEmployeeById(empId);

        // Security check: verify this employee is assigned to this manager (unless Admin)
        if (!"ADMIN".equalsIgnoreCase(manager.getRole()) && (employee.getManagerId() == null || !employee.getManagerId().equals(manager.getId()))) {
            request.getSession().setAttribute("flashError", "You are not authorized to evaluate this employee.");
            response.sendRedirect(request.getContextPath() + "/manager/team");
            return;
        }

        EvaluationCycle activeCycle = cycleService.getActiveCycle();
        if (activeCycle == null) {
            request.getSession().setAttribute("flashError", "There is currently no active evaluation cycle open.");
            response.sendRedirect(request.getContextPath() + "/manager/team");
            return;
        }

        List<EvaluationCriterion> criteria = criterionService.getActiveCriteria();
        Evaluation existingEval = evaluationService.getEvaluationForEmployeeAndCycle(empId, activeCycle.getId());

        // Map existing scores by criterion id for prepopulation
        Map<Integer, EvaluationScore> scoreMap = new HashMap<>();
        if (existingEval != null && existingEval.getScores() != null) {
            for (EvaluationScore es : existingEval.getScores()) {
                scoreMap.put(es.getCriterionId(), es);
            }
        }

        request.setAttribute("employee", employee);
        request.setAttribute("cycle", activeCycle);
        request.setAttribute("criteria", criteria);
        request.setAttribute("evaluation", existingEval);
        request.setAttribute("scoreMap", scoreMap);

        request.getRequestDispatcher("/WEB-INF/views/manager/evaluate.jsp").forward(request, response);
    }

    private void showEvaluationView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/manager/team");
            return;
        }

        int evalId = Integer.parseInt(idParam);
        Evaluation evaluation = evaluationService.getEvaluationById(evalId);

        request.setAttribute("evaluation", evaluation);
        request.getRequestDispatcher("/WEB-INF/views/manager/evaluation-view.jsp").forward(request, response);
    }

    private void handleEvaluatePost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User manager = getCurrentManager(request);
        String action = request.getParameter("action"); // 'draft' or 'submit'
        boolean isFinalSubmit = "submit".equalsIgnoreCase(action);

        try {
            int employeeId = Integer.parseInt(request.getParameter("employeeId"));
            int cycleId = Integer.parseInt(request.getParameter("cycleId"));

            String evalIdParam = request.getParameter("evaluationId");
            Integer evaluationId = (evalIdParam != null && !evalIdParam.trim().isEmpty()) ? Integer.parseInt(evalIdParam) : null;

            String overallFeedback = request.getParameter("overallFeedback");
            String strengths = request.getParameter("strengths");
            String improvements = request.getParameter("improvements");

            List<EvaluationCriterion> criteria = criterionService.getActiveCriteria();
            List<EvaluationScore> scores = new ArrayList<>();

            for (EvaluationCriterion ec : criteria) {
                String scoreStr = request.getParameter("score_" + ec.getId());
                String comment = request.getParameter("comment_" + ec.getId());

                int score = 3; // default moderate score
                if (scoreStr != null && !scoreStr.trim().isEmpty()) {
                    try {
                        score = Integer.parseInt(scoreStr);
                    } catch (NumberFormatException ignored) {
                    }
                }

                EvaluationScore es = new EvaluationScore();
                es.setCriterionId(ec.getId());
                es.setCriterionName(ec.getName());
                es.setCriterionWeight(ec.getWeight());
                es.setScore(score);
                es.setComments(comment != null ? comment.trim() : "");
                scores.add(es);
            }

            Evaluation saved = evaluationService.saveOrSubmitEvaluation(
                    evaluationId, employeeId, manager.getId(), cycleId, scores,
                    overallFeedback, strengths, improvements, isFinalSubmit
            );

            if (isFinalSubmit) {
                request.getSession().setAttribute("flashSuccess", 
                        "Evaluation submitted successfully! Total Score: " + saved.getTotalScore() + 
                        " (" + saved.getRatingLabel() + ")");
            } else {
                request.getSession().setAttribute("flashSuccess", "Evaluation draft saved successfully.");
            }

        } catch (ValidationException ve) {
            request.getSession().setAttribute("flashError", ve.getMessage());
        } catch (Exception ex) {
            request.getSession().setAttribute("flashError", "Failed to save evaluation: " + ex.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/manager/team");
    }
}
