package com.eps.dao;

import com.eps.model.EvaluationCycle;

import java.util.List;

/**
 * Data Access Object for Evaluation Cycle operations.
 */
public interface EvaluationCycleDAO extends GenericDAO<EvaluationCycle, Integer> {

    EvaluationCycle findActiveCycle();

    List<EvaluationCycle> findRecentCycles(int limit);

    boolean updateStatus(Integer cycleId, String status);
}
