package com.eps.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Individual score and feedback comment assigned to a specific criterion during an appraisal.
 */
public class EvaluationScore implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer evaluationId;
    private Integer criterionId;
    private String criterionName;
    private String criterionDescription;
    private BigDecimal criterionWeight; // e.g. 20.00
    private int score; // 1 to 5
    private String comments;

    public EvaluationScore() {
    }

    public EvaluationScore(Integer id, Integer evaluationId, Integer criterionId, 
                           String criterionName, BigDecimal criterionWeight, 
                           int score, String comments) {
        this.id = id;
        this.evaluationId = evaluationId;
        this.criterionId = criterionId;
        this.criterionName = criterionName;
        this.criterionWeight = criterionWeight;
        this.score = score;
        this.comments = comments;
    }

    /**
     * Calculates weighted contribution of this score (e.g. 5 * 20% = 1.00).
     */
    public BigDecimal getWeightedScore() {
        if (criterionWeight == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(score)
                .multiply(criterionWeight)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Integer evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Integer getCriterionId() {
        return criterionId;
    }

    public void setCriterionId(Integer criterionId) {
        this.criterionId = criterionId;
    }

    public String getCriterionName() {
        return criterionName;
    }

    public void setCriterionName(String criterionName) {
        this.criterionName = criterionName;
    }

    public String getCriterionDescription() {
        return criterionDescription;
    }

    public void setCriterionDescription(String criterionDescription) {
        this.criterionDescription = criterionDescription;
    }

    public BigDecimal getCriterionWeight() {
        return criterionWeight;
    }

    public void setCriterionWeight(BigDecimal criterionWeight) {
        this.criterionWeight = criterionWeight;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
