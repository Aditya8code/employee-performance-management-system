package com.eps.service.impl;

import com.eps.dao.EvaluationCriterionDAO;
import com.eps.dao.impl.EvaluationCriterionDAOImpl;
import com.eps.exception.ResourceNotFoundException;
import com.eps.exception.ValidationException;
import com.eps.model.EvaluationCriterion;
import com.eps.service.EvaluationCriterionService;
import com.eps.util.ValidationUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementation of EvaluationCriterionService.
 */
public class EvaluationCriterionServiceImpl implements EvaluationCriterionService {

    private final EvaluationCriterionDAO criterionDAO;

    public EvaluationCriterionServiceImpl() {
        this.criterionDAO = new EvaluationCriterionDAOImpl();
    }

    public EvaluationCriterionServiceImpl(EvaluationCriterionDAO criterionDAO) {
        this.criterionDAO = criterionDAO;
    }

    @Override
    public List<EvaluationCriterion> getAllCriteria() {
        return criterionDAO.findAll();
    }

    @Override
    public List<EvaluationCriterion> getActiveCriteria() {
        return criterionDAO.findAllActive();
    }

    @Override
    public EvaluationCriterion getCriterionById(Integer id) {
        EvaluationCriterion ec = criterionDAO.findById(id);
        if (ec == null) {
            throw new ResourceNotFoundException("Evaluation criterion with id " + id + " not found.");
        }
        return ec;
    }

    @Override
    public EvaluationCriterion createCriterion(String name, String description, BigDecimal weight) {
        if (!ValidationUtil.isNotEmpty(name)) {
            throw new ValidationException("Criterion name is required.");
        }
        if (!ValidationUtil.isValidWeight(weight)) {
            throw new ValidationException("Weight must be greater than 0% and up to 100%.");
        }

        EvaluationCriterion ec = new EvaluationCriterion();
        ec.setName(name.trim());
        ec.setDescription(description != null ? description.trim() : null);
        ec.setWeight(weight);
        ec.setActive(true);

        return criterionDAO.save(ec);
    }

    @Override
    public void updateCriterion(Integer id, String name, String description, BigDecimal weight, boolean active) {
        EvaluationCriterion ec = getCriterionById(id);

        if (!ValidationUtil.isNotEmpty(name)) {
            throw new ValidationException("Criterion name is required.");
        }
        if (!ValidationUtil.isValidWeight(weight)) {
            throw new ValidationException("Weight must be greater than 0% and up to 100%.");
        }

        ec.setName(name.trim());
        ec.setDescription(description != null ? description.trim() : null);
        ec.setWeight(weight);
        ec.setActive(active);

        criterionDAO.update(ec);
    }

    @Override
    public void deleteCriterion(Integer id) {
        getCriterionById(id);
        criterionDAO.deleteById(id);
    }
}
