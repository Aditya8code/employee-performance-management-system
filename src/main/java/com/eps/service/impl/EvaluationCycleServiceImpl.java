package com.eps.service.impl;

import com.eps.dao.EvaluationCycleDAO;
import com.eps.dao.impl.EvaluationCycleDAOImpl;
import com.eps.exception.ResourceNotFoundException;
import com.eps.exception.ValidationException;
import com.eps.model.EvaluationCycle;
import com.eps.service.EvaluationCycleService;
import com.eps.util.ValidationUtil;

import java.sql.Date;
import java.util.List;

/**
 * Implementation of EvaluationCycleService.
 */
public class EvaluationCycleServiceImpl implements EvaluationCycleService {

    private final EvaluationCycleDAO cycleDAO;

    public EvaluationCycleServiceImpl() {
        this.cycleDAO = new EvaluationCycleDAOImpl();
    }

    public EvaluationCycleServiceImpl(EvaluationCycleDAO cycleDAO) {
        this.cycleDAO = cycleDAO;
    }

    @Override
    public List<EvaluationCycle> getAllCycles() {
        return cycleDAO.findAll();
    }

    @Override
    public EvaluationCycle getCycleById(Integer id) {
        EvaluationCycle cycle = cycleDAO.findById(id);
        if (cycle == null) {
            throw new ResourceNotFoundException("Evaluation cycle with id " + id + " not found.");
        }
        return cycle;
    }

    @Override
    public EvaluationCycle getActiveCycle() {
        return cycleDAO.findActiveCycle();
    }

    @Override
    public EvaluationCycle createCycle(String name, String description, Date startDate, Date endDate) {
        if (!ValidationUtil.isNotEmpty(name)) {
            throw new ValidationException("Cycle name is required.");
        }
        if (startDate == null || endDate == null) {
            throw new ValidationException("Start date and end date are required.");
        }
        if (startDate.after(endDate)) {
            throw new ValidationException("Start date cannot be after end date.");
        }

        EvaluationCycle cycle = new EvaluationCycle();
        cycle.setName(name.trim());
        cycle.setDescription(description != null ? description.trim() : null);
        cycle.setStartDate(startDate);
        cycle.setEndDate(endDate);
        cycle.setStatus("DRAFT");

        return cycleDAO.save(cycle);
    }

    @Override
    public void updateCycle(Integer id, String name, String description, Date startDate, Date endDate, String status) {
        EvaluationCycle cycle = getCycleById(id);

        if (!ValidationUtil.isNotEmpty(name)) {
            throw new ValidationException("Cycle name is required.");
        }
        if (startDate == null || endDate == null) {
            throw new ValidationException("Start date and end date are required.");
        }
        if (startDate.after(endDate)) {
            throw new ValidationException("Start date cannot be after end date.");
        }

        cycle.setName(name.trim());
        cycle.setDescription(description != null ? description.trim() : null);
        cycle.setStartDate(startDate);
        cycle.setEndDate(endDate);
        if (status != null && !status.trim().isEmpty()) {
            cycle.setStatus(status.trim().toUpperCase());
        }

        cycleDAO.update(cycle);
    }

    @Override
    public void changeCycleStatus(Integer id, String status) {
        getCycleById(id);
        if (status == null || (!"DRAFT".equalsIgnoreCase(status) && !"ACTIVE".equalsIgnoreCase(status) && !"COMPLETED".equalsIgnoreCase(status))) {
            throw new ValidationException("Invalid cycle status: " + status);
        }
        cycleDAO.updateStatus(id, status.toUpperCase());
    }

    @Override
    public void deleteCycle(Integer id) {
        getCycleById(id);
        cycleDAO.deleteById(id);
    }
}
