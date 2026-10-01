package com.eps.service;

import com.eps.model.DepartmentStats;
import com.eps.model.Evaluation;
import com.eps.model.ReportFilter;

import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * Service interface for organization reports, analytics, and CSV streaming.
 */
public interface ReportService {

    List<Evaluation> getFilteredEvaluations(ReportFilter filter);

    List<DepartmentStats> getDepartmentStats(Integer cycleId);

    void exportReportCSV(ReportFilter filter, Writer writer);

    Map<String, Object> getAdminDashboardStats();
}
