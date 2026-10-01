package com.eps.dao;

import com.eps.model.DepartmentStats;
import com.eps.model.Evaluation;
import com.eps.model.EvaluationScore;
import com.eps.model.ReportFilter;

import java.util.List;

/**
 * Data Access Object for Evaluation and Score operations.
 */
public interface EvaluationDAO extends GenericDAO<Evaluation, Integer> {

    Evaluation findByEmployeeAndCycle(Integer employeeId, Integer cycleId);

    List<Evaluation> findByEmployeeId(Integer employeeId);

    List<Evaluation> findByManagerAndCycle(Integer managerId, Integer cycleId);

    List<Evaluation> findByCycleId(Integer cycleId);

    List<Evaluation> searchEvaluations(ReportFilter filter);

    List<EvaluationScore> findScoresByEvaluationId(Integer evaluationId);

    boolean saveScores(Integer evaluationId, List<EvaluationScore> scores);

    List<DepartmentStats> getDepartmentPerformanceStats(Integer cycleId);

    int countEvaluationsByStatus(Integer cycleId, String status);

    Double getAverageCompanyScore(Integer cycleId);
}
