package com.eps.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Aggregated performance metrics for a department used in organizational reports and Chart.js graphs.
 */
public class DepartmentStats implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer departmentId;
    private String departmentName;
    private int totalEmployees;
    private int completedEvaluations;
    private int pendingEvaluations;
    private BigDecimal averageScore;
    private int outstandingCount;
    private int exceedsCount;
    private int meetsCount;
    private int needsImprovementCount;
    private int unsatisfactoryCount;

    public DepartmentStats() {
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(int totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public int getCompletedEvaluations() {
        return completedEvaluations;
    }

    public void setCompletedEvaluations(int completedEvaluations) {
        this.completedEvaluations = completedEvaluations;
    }

    public int getPendingEvaluations() {
        return pendingEvaluations;
    }

    public void setPendingEvaluations(int pendingEvaluations) {
        this.pendingEvaluations = pendingEvaluations;
    }

    public BigDecimal getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(BigDecimal averageScore) {
        this.averageScore = averageScore;
    }

    public int getOutstandingCount() {
        return outstandingCount;
    }

    public void setOutstandingCount(int outstandingCount) {
        this.outstandingCount = outstandingCount;
    }

    public int getExceedsCount() {
        return exceedsCount;
    }

    public void setExceedsCount(int exceedsCount) {
        this.exceedsCount = exceedsCount;
    }

    public int getMeetsCount() {
        return meetsCount;
    }

    public void setMeetsCount(int meetsCount) {
        this.meetsCount = meetsCount;
    }

    public int getNeedsImprovementCount() {
        return needsImprovementCount;
    }

    public void setNeedsImprovementCount(int needsImprovementCount) {
        this.needsImprovementCount = needsImprovementCount;
    }

    public int getUnsatisfactoryCount() {
        return unsatisfactoryCount;
    }

    public void setUnsatisfactoryCount(int unsatisfactoryCount) {
        this.unsatisfactoryCount = unsatisfactoryCount;
    }
}
