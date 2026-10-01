package com.eps.dao;

import com.eps.model.Employee;

import java.util.List;

/**
 * Data Access Object for Employee operations.
 */
public interface EmployeeDAO extends GenericDAO<Employee, Integer> {

    Employee findByUserId(Integer userId);

    List<Employee> findByDepartmentId(Integer departmentId);

    List<Employee> findByManagerId(Integer managerId);

    boolean assignManager(Integer employeeId, Integer managerId);

    int countTotalEmployees();
}
