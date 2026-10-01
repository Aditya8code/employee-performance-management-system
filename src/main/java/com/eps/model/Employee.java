package com.eps.model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Employee domain entity representing an individual staff member.
 * Demonstrates OOP Inheritance (extends User) and rich encapsulation.
 */
public class Employee extends User {
    private static final long serialVersionUID = 1L;

    private Integer employeeId; // Primary key in employees table
    private Integer departmentId;
    private String departmentName;
    private Integer managerId;
    private String managerName;
    private String jobTitle;
    private Date hireDate;
    private BigDecimal salary;

    public Employee() {
        super();
        setRole("EMPLOYEE");
    }

    public Employee(Integer userId, String username, String passwordHash, 
                    String fullName, String email, String phone, String status,
                    Integer employeeId, Integer departmentId, String departmentName,
                    Integer managerId, String managerName, String jobTitle,
                    Date hireDate, BigDecimal salary) {
        super(userId, username, passwordHash, "EMPLOYEE", fullName, email, phone, status);
        this.employeeId = employeeId;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.managerId = managerId;
        this.managerName = managerName;
        this.jobTitle = jobTitle;
        this.hireDate = hireDate;
        this.salary = salary;
    }

    @Override
    public String getRoleDisplayName() {
        return "Employee";
    }

    @Override
    public String getDefaultDashboardUrl() {
        return "/employee/dashboard";
    }

    // Getters and Setters
    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
