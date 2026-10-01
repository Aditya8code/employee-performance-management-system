package com.eps.dao;

import com.eps.model.Department;

/**
 * Data Access Object for Department operations.
 */
public interface DepartmentDAO extends GenericDAO<Department, Integer> {

    Department findByName(String name);

    int countEmployeesInDepartment(Integer departmentId);
}
