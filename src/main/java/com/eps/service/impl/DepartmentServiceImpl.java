package com.eps.service.impl;

import com.eps.dao.DepartmentDAO;
import com.eps.dao.impl.DepartmentDAOImpl;
import com.eps.exception.ResourceNotFoundException;
import com.eps.exception.ValidationException;
import com.eps.model.Department;
import com.eps.service.DepartmentService;
import com.eps.util.ValidationUtil;

import java.util.List;

/**
 * Implementation of DepartmentService.
 */
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentDAO departmentDAO;

    public DepartmentServiceImpl() {
        this.departmentDAO = new DepartmentDAOImpl();
    }

    public DepartmentServiceImpl(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentDAO.findAll();
    }

    @Override
    public Department getDepartmentById(Integer id) {
        Department dept = departmentDAO.findById(id);
        if (dept == null) {
            throw new ResourceNotFoundException("Department with id " + id + " not found.");
        }
        return dept;
    }

    @Override
    public Department createDepartment(String name, String description) {
        if (!ValidationUtil.isNotEmpty(name)) {
            throw new ValidationException("Department name is required.");
        }

        Department existing = departmentDAO.findByName(name.trim());
        if (existing != null) {
            throw new ValidationException("A department with name '" + name.trim() + "' already exists.");
        }

        Department dept = new Department();
        dept.setName(name.trim());
        dept.setDescription(description != null ? description.trim() : null);

        return departmentDAO.save(dept);
    }

    @Override
    public void updateDepartment(Integer id, String name, String description) {
        if (!ValidationUtil.isNotEmpty(name)) {
            throw new ValidationException("Department name is required.");
        }

        Department dept = getDepartmentById(id);
        Department existing = departmentDAO.findByName(name.trim());
        if (existing != null && !existing.getId().equals(id)) {
            throw new ValidationException("A department with name '" + name.trim() + "' already exists.");
        }

        dept.setName(name.trim());
        dept.setDescription(description != null ? description.trim() : null);
        departmentDAO.update(dept);
    }

    @Override
    public void deleteDepartment(Integer id) {
        Department dept = getDepartmentById(id);
        int empCount = departmentDAO.countEmployeesInDepartment(id);
        if (empCount > 0) {
            throw new ValidationException("Cannot delete department '" + dept.getName() + "' because " + empCount + " employees are assigned to it.");
        }
        departmentDAO.deleteById(id);
    }
}
