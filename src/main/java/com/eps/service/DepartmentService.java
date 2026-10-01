package com.eps.service;

import com.eps.model.Department;

import java.util.List;

/**
 * Service interface for Department administration.
 */
public interface DepartmentService {

    List<Department> getAllDepartments();

    Department getDepartmentById(Integer id);

    Department createDepartment(String name, String description);

    void updateDepartment(Integer id, String name, String description);

    void deleteDepartment(Integer id);
}
