package com.eps.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents a key evaluation criterion (e.g. Attendance, Work Quality, Productivity, Teamwork, Communication, Goal Achievement).
 */
public class EvaluationCriterion implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String description;
    private BigDecimal weight; // Percentage, e.g. 20.00%
    private boolean active;
    private Timestamp createdAt;

    public EvaluationCriterion() {
    }

    public EvaluationCriterion(Integer id, String name, String description, BigDecimal weight, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.active = active;
    }

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

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
