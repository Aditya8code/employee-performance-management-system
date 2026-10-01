package com.eps.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager domain entity representing an appraisal evaluator and team lead.
 * Demonstrates OOP Inheritance and Polymorphism.
 */
public class Manager extends User {
    private static final long serialVersionUID = 1L;

    private List<Employee> directReports = new ArrayList<>();
    private int teamCount;

    public Manager() {
        super();
        setRole("MANAGER");
    }

    public Manager(Integer id, String username, String passwordHash, 
                   String fullName, String email, String phone, String status) {
        super(id, username, passwordHash, "MANAGER", fullName, email, phone, status);
    }

    @Override
    public String getRoleDisplayName() {
        return "Manager";
    }

    @Override
    public String getDefaultDashboardUrl() {
        return "/manager/dashboard";
    }

    public List<Employee> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(List<Employee> directReports) {
        this.directReports = directReports;
        this.teamCount = (directReports != null) ? directReports.size() : 0;
    }

    public int getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
    }
}
