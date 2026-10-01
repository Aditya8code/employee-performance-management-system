package com.eps.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain entity representing an employee performance evaluation appraisal.
 * Encapsulates the overall appraisal, criterion scores, weighted score calculation,
 * and status lifecycle.
 */
public class Evaluation implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer employeeId;
    private String employeeName;
    private String employeeEmail;
    private String employeeTitle;
    private String departmentName;

    private Integer managerId;
    private String managerName;

    private Integer cycleId;
    private String cycleName;
    private String cycleStatus;

    private BigDecimal totalScore;
    private String ratingLabel;
    private String overallFeedback;
    private String strengths;
    private String improvements;
    private String status; // DRAFT, SUBMITTED, APPROVED
    private Timestamp submittedAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private List<EvaluationScore> scores = new ArrayList<>();

    public Evaluation() {
    }

    /**
     * Domain business method: calculates weighted total score from criterion scores.
     * Score = Sum(criterion_score * weight / 100)
     */
    public void calculateTotalScore() {
        if (scores == null || scores.isEmpty()) {
            this.totalScore = null;
            this.ratingLabel = null;
            return;
        }

        BigDecimal sumWeighted = BigDecimal.ZERO;
        BigDecimal sumWeights = BigDecimal.ZERO;

        for (EvaluationScore es : scores) {
            if (es.getCriterionWeight() != null) {
                BigDecimal weight = es.getCriterionWeight();
                sumWeights = sumWeights.add(weight);
                sumWeighted = sumWeighted.add(BigDecimal.valueOf(es.getScore()).multiply(weight));
            }
        }

        if (sumWeights.compareTo(BigDecimal.ZERO) > 0) {
            // Normalized in case weights don't sum to exactly 100%
            BigDecimal finalScore = sumWeighted.divide(sumWeights, 2, RoundingMode.HALF_UP);
            this.totalScore = finalScore;
            this.ratingLabel = RatingScale.fromScore(finalScore.doubleValue()).getLabel();
        }
    }

    public boolean isSubmitted() {
        return "SUBMITTED".equalsIgnoreCase(status) || "APPROVED".equalsIgnoreCase(status);
    }

    public boolean isDraft() {
        return "DRAFT".equalsIgnoreCase(status);
    }

    public String getStatusBadgeClass() {
        if ("APPROVED".equalsIgnoreCase(status) || "SUBMITTED".equalsIgnoreCase(status)) {
            return "bg-success text-white";
        } else if ("DRAFT".equalsIgnoreCase(status)) {
            return "bg-warning text-dark";
        }
        return "bg-secondary text-white";
    }

    public String getRatingBadgeClass() {
        return RatingScale.getBadgeClassForLabel(this.ratingLabel);
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeeTitle() {
        return employeeTitle;
    }

    public void setEmployeeTitle(String employeeTitle) {
        this.employeeTitle = employeeTitle;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Integer getCycleId() {
        return cycleId;
    }

    public void setCycleId(Integer cycleId) {
        this.cycleId = cycleId;
    }

    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

    public String getCycleStatus() {
        return cycleStatus;
    }

    public void setCycleStatus(String cycleStatus) {
        this.cycleStatus = cycleStatus;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public String getRatingLabel() {
        return ratingLabel;
    }

    public void setRatingLabel(String ratingLabel) {
        this.ratingLabel = ratingLabel;
    }

    public String getOverallFeedback() {
        return overallFeedback;
    }

    public void setOverallFeedback(String overallFeedback) {
        this.overallFeedback = overallFeedback;
    }

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public String getImprovements() {
        return improvements;
    }

    public void setImprovements(String improvements) {
        this.improvements = improvements;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<EvaluationScore> getScores() {
        return scores;
    }

    public void setScores(List<EvaluationScore> scores) {
        this.scores = scores;
    }
}
