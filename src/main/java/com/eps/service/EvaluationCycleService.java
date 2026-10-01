package com.eps.service;

import com.eps.model.EvaluationCycle;

import java.sql.Date;
import java.util.List;

/**
 * Service interface for Evaluation Cycle lifecycle management.
 */
public interface EvaluationCycleService {

    List<EvaluationCycle> getAllCycles();

    EvaluationCycle getCycleById(Integer id);

    EvaluationCycle getActiveCycle();

    EvaluationCycle createCycle(String name, String description, Date startDate, Date endDate);

    void updateCycle(Integer id, String name, String description, Date startDate, Date endDate, String status);

    void changeCycleStatus(Integer id, String status);

    void deleteCycle(Integer id);
}
