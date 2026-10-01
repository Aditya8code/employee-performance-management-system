package com.eps.service;

import com.eps.model.EvaluationCriterion;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Criteria management.
 */
public interface EvaluationCriterionService {

    List<EvaluationCriterion> getAllCriteria();

    List<EvaluationCriterion> getActiveCriteria();

    EvaluationCriterion getCriterionById(Integer id);

    EvaluationCriterion createCriterion(String name, String description, BigDecimal weight);

    void updateCriterion(Integer id, String name, String description, BigDecimal weight, boolean active);

    void deleteCriterion(Integer id);
}
