package com.eps.service;

import com.eps.model.Employee;
import com.eps.model.User;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * Service interface for Employee administration and manager hierarchy.
 */
public interface EmployeeService {

    List<Employee> getAllEmployees();

    Employee getEmployeeById(Integer employeeId);

    Employee getEmployeeByUserId(Integer userId);

    List<Employee> getEmployeesByDepartment(Integer departmentId);

    List<Employee> getEmployeesByManager(Integer managerId);

    List<User> getAllManagers();

    Employee createEmployee(String username, String plainPassword, String fullName, 
                           String email, String phone, Integer departmentId, 
                           Integer managerId, String jobTitle, Date hireDate, BigDecimal salary);

    void updateEmployee(Integer employeeId, String fullName, String email, String phone, 
                        Integer departmentId, Integer managerId, String jobTitle, 
                        Date hireDate, BigDecimal salary, String status);

    void assignManager(Integer employeeId, Integer managerId);

    void deleteEmployee(Integer employeeId);

    int getTotalEmployeeCount();
}
