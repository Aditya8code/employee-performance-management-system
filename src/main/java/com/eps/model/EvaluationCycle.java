package com.eps.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Represents a review cycle (e.g. Annual Review 2025, Mid-Year 2026).
 */
public class EvaluationCycle implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private String status; // DRAFT, ACTIVE, COMPLETED
    private int totalEvaluations;
    private int completedEvaluations;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public EvaluationCycle() {
    }

    public EvaluationCycle(Integer id, String name, String description, 
                           Date startDate, Date endDate, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status);
    }

    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(this.status);
    }

    public boolean isDraft() {
        return "DRAFT".equalsIgnoreCase(this.status);
    }

    public String getStatusBadgeClass() {
        if ("ACTIVE".equalsIgnoreCase(this.status)) {
            return "bg-success";
        } else if ("COMPLETED".equalsIgnoreCase(this.status)) {
            return "bg-secondary";
        } else {
            return "bg-warning text-dark";
        }
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalEvaluations() {
        return totalEvaluations;
    }

    public void setTotalEvaluations(int totalEvaluations) {
        this.totalEvaluations = totalEvaluations;
    }

    public int getCompletedEvaluations() {
        return completedEvaluations;
    }

    public void setCompletedEvaluations(int completedEvaluations) {
        this.completedEvaluations = completedEvaluations;
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
}
