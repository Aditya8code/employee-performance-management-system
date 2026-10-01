package com.eps.dao;

import com.eps.model.EvaluationCriterion;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Access Object for Evaluation Criteria operations.
 */
public interface EvaluationCriterionDAO extends GenericDAO<EvaluationCriterion, Integer> {

    List<EvaluationCriterion> findAllActive();

    BigDecimal calculateTotalActiveWeight();
}
