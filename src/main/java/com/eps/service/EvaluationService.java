package com.eps.service;

import com.eps.model.Evaluation;
import com.eps.model.EvaluationScore;

import java.util.List;

/**
 * Service interface for appraisals, scoring calculation, and status progression.
 */
public interface EvaluationService {

    Evaluation getEvaluationById(Integer id);

    Evaluation getEvaluationForEmployeeAndCycle(Integer employeeId, Integer cycleId);

    List<Evaluation> getEmployeeEvaluations(Integer employeeId);

    List<Evaluation> getTeamEvaluationsForManager(Integer managerId, Integer cycleId);

    Evaluation saveOrSubmitEvaluation(Integer evaluationId, Integer employeeId, Integer managerId, 
                                     Integer cycleId, List<EvaluationScore> scores, 
                                     String overallFeedback, String strengths, 
                                     String improvements, boolean isFinalSubmit);
}
