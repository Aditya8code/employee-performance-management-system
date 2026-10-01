package com.eps.service.impl;

import com.eps.dao.EvaluationCriterionDAO;
import com.eps.dao.EvaluationDAO;
import com.eps.dao.impl.EvaluationCriterionDAOImpl;
import com.eps.dao.impl.EvaluationDAOImpl;
import com.eps.exception.ResourceNotFoundException;
import com.eps.exception.ValidationException;
import com.eps.model.Evaluation;
import com.eps.model.EvaluationCriterion;
import com.eps.model.EvaluationScore;
import com.eps.service.EvaluationService;
import com.eps.util.ScoreCalculator;
import com.eps.util.ValidationUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of EvaluationService.
 * Executes appraisal calculations, scoring validations, and status lifecycles.
 */
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationDAO evaluationDAO;
    private final EvaluationCriterionDAO criterionDAO;

    public EvaluationServiceImpl() {
        this.evaluationDAO = new EvaluationDAOImpl();
        this.criterionDAO = new EvaluationCriterionDAOImpl();
    }

    public EvaluationServiceImpl(EvaluationDAO evaluationDAO, EvaluationCriterionDAO criterionDAO) {
        this.evaluationDAO = evaluationDAO;
        this.criterionDAO = criterionDAO;
    }

    @Override
    public Evaluation getEvaluationById(Integer id) {
        Evaluation eval = evaluationDAO.findById(id);
        if (eval == null) {
            throw new ResourceNotFoundException("Evaluation appraisal with id " + id + " not found.");
        }
        return eval;
    }

    @Override
    public Evaluation getEvaluationForEmployeeAndCycle(Integer employeeId, Integer cycleId) {
        return evaluationDAO.findByEmployeeAndCycle(employeeId, cycleId);
    }

    @Override
    public List<Evaluation> getEmployeeEvaluations(Integer employeeId) {
        return evaluationDAO.findByEmployeeId(employeeId);
    }

    @Override
    public List<Evaluation> getTeamEvaluationsForManager(Integer managerId, Integer cycleId) {
        return evaluationDAO.findByManagerAndCycle(managerId, cycleId);
    }

    @Override
    public Evaluation saveOrSubmitEvaluation(Integer evaluationId, Integer employeeId, Integer managerId, 
                                           Integer cycleId, List<EvaluationScore> scores, 
                                           String overallFeedback, String strengths, 
                                           String improvements, boolean isFinalSubmit) {
        if (employeeId == null || managerId == null || cycleId == null) {
            throw new ValidationException("Employee, manager, and evaluation cycle must be specified.");
        }

        // Fetch active criteria to map accurate weights
        List<EvaluationCriterion> criteriaList = criterionDAO.findAllActive();
        Map<Integer, EvaluationCriterion> criterionMap = criteriaList.stream()
                .collect(Collectors.toMap(EvaluationCriterion::getId, Function.identity()));

        if (scores != null) {
            for (EvaluationScore es : scores) {
                if (!ValidationUtil.isValidScore(es.getScore())) {
                    throw new ValidationException("All evaluation scores must be on a scale of 1 to 5.");
                }
                EvaluationCriterion criterion = criterionMap.get(es.getCriterionId());
                if (criterion != null) {
                    es.setCriterionName(criterion.getName());
                    es.setCriterionDescription(criterion.getDescription());
                    es.setCriterionWeight(criterion.getWeight());
                }
            }
        }

        Evaluation eval = new Evaluation();
        if (evaluationId != null && evaluationId > 0) {
            eval.setId(evaluationId);
        } else {
            Evaluation existing = evaluationDAO.findByEmployeeAndCycle(employeeId, cycleId);
            if (existing != null) {
                eval.setId(existing.getId());
            }
        }

        eval.setEmployeeId(employeeId);
        eval.setManagerId(managerId);
        eval.setCycleId(cycleId);
        eval.setOverallFeedback(overallFeedback != null ? overallFeedback.trim() : null);
        eval.setStrengths(strengths != null ? strengths.trim() : null);
        eval.setImprovements(improvements != null ? improvements.trim() : null);
        eval.setScores(scores);

        // Calculate weighted score & rating label
        eval.calculateTotalScore();

        if (isFinalSubmit) {
            if (scores == null || scores.isEmpty()) {
                throw new ValidationException("Cannot submit evaluation without scoring all criteria.");
            }
            eval.setStatus("SUBMITTED");
            eval.setSubmittedAt(new Timestamp(System.currentTimeMillis()));
        } else {
            eval.setStatus("DRAFT");
            eval.setSubmittedAt(null);
        }

        return evaluationDAO.save(eval);
    }
}
