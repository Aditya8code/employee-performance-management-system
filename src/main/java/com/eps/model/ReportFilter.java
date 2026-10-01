package com.eps.model;

import java.io.Serializable;

/**
 * Encapsulates filter parameters for organization performance reports and CSV exports.
 */
public class ReportFilter implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer cycleId;
    private Integer departmentId;
    private String ratingLabel;
    private String status; // ALL, SUBMITTED, DRAFT, PENDING

    public ReportFilter() {
    }

    public ReportFilter(Integer cycleId, Integer departmentId, String ratingLabel, String status) {
        this.cycleId = cycleId;
        this.departmentId = departmentId;
        this.ratingLabel = ratingLabel;
        this.status = status;
    }

    public Integer getCycleId() {
        return cycleId;
    }

    public void setCycleId(Integer cycleId) {
        this.cycleId = cycleId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getRatingLabel() {
        return ratingLabel;
    }

    public void setRatingLabel(String ratingLabel) {
        this.ratingLabel = ratingLabel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
