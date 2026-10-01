package com.eps.service.impl;

import com.eps.dao.DepartmentDAO;
import com.eps.dao.EmployeeDAO;
import com.eps.dao.EvaluationCycleDAO;
import com.eps.dao.EvaluationDAO;
import com.eps.dao.impl.DepartmentDAOImpl;
import com.eps.dao.impl.EmployeeDAOImpl;
import com.eps.dao.impl.EvaluationCycleDAOImpl;
import com.eps.dao.impl.EvaluationDAOImpl;
import com.eps.model.DepartmentStats;
import com.eps.model.Evaluation;
import com.eps.model.EvaluationCycle;
import com.eps.model.ReportFilter;
import com.eps.service.ReportService;
import com.eps.util.CSVExporter;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of ReportService for organization analytics, dashboard metrics, and CSV reporting.
 */
public class ReportServiceImpl implements ReportService {

    private final EvaluationDAO evaluationDAO;
    private final EvaluationCycleDAO cycleDAO;
    private final DepartmentDAO departmentDAO;
    private final EmployeeDAO employeeDAO;

    public ReportServiceImpl() {
        this.evaluationDAO = new EvaluationDAOImpl();
        this.cycleDAO = new EvaluationCycleDAOImpl();
        this.departmentDAO = new DepartmentDAOImpl();
        this.employeeDAO = new EmployeeDAOImpl();
    }

    public ReportServiceImpl(EvaluationDAO evaluationDAO, EvaluationCycleDAO cycleDAO, 
                             DepartmentDAO departmentDAO, EmployeeDAO employeeDAO) {
        this.evaluationDAO = evaluationDAO;
        this.cycleDAO = cycleDAO;
        this.departmentDAO = departmentDAO;
        this.employeeDAO = employeeDAO;
    }

    @Override
    public List<Evaluation> getFilteredEvaluations(ReportFilter filter) {
        if (filter == null) {
            filter = new ReportFilter();
        }
        return evaluationDAO.searchEvaluations(filter);
    }

    @Override
    public List<DepartmentStats> getDepartmentStats(Integer cycleId) {
        return evaluationDAO.getDepartmentPerformanceStats(cycleId);
    }

    @Override
    public void exportReportCSV(ReportFilter filter, Writer writer) {
        List<Evaluation> data = getFilteredEvaluations(filter);
        CSVExporter.exportEvaluationsToCSV(data, writer);
    }

    @Override
    public Map<String, Object> getAdminDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        int totalEmployees = employeeDAO.countTotalEmployees();
        stats.put("totalEmployees", totalEmployees);
        stats.put("totalDepartments", departmentDAO.findAll().size());

        EvaluationCycle activeCycle = cycleDAO.findActiveCycle();
        stats.put("activeCycle", activeCycle);

        if (activeCycle != null) {
            int completed = evaluationDAO.countEvaluationsByStatus(activeCycle.getId(), "SUBMITTED");
            completed += evaluationDAO.countEvaluationsByStatus(activeCycle.getId(), "APPROVED");
            int totalExpected = totalEmployees;
            int pending = Math.max(0, totalExpected - completed);

            stats.put("completedEvaluations", completed);
            stats.put("pendingEvaluations", pending);

            Double avgScore = evaluationDAO.getAverageCompanyScore(activeCycle.getId());
            stats.put("averageCompanyScore", avgScore != null ? String.format("%.2f", avgScore) : "N/A");
        } else {
            stats.put("completedEvaluations", 0);
            stats.put("pendingEvaluations", 0);
            stats.put("averageCompanyScore", "N/A");
        }

        return stats;
    }
}
