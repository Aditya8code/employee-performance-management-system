package com.eps.service.impl;

import com.eps.dao.DepartmentDAO;
import com.eps.dao.EmployeeDAO;
import com.eps.dao.UserDAO;
import com.eps.dao.impl.DepartmentDAOImpl;
import com.eps.dao.impl.EmployeeDAOImpl;
import com.eps.dao.impl.UserDAOImpl;
import com.eps.exception.ResourceNotFoundException;
import com.eps.exception.ValidationException;
import com.eps.model.Employee;
import com.eps.model.User;
import com.eps.service.EmployeeService;
import com.eps.util.PasswordUtil;
import com.eps.util.ValidationUtil;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * Implementation of EmployeeService.
 */
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDAO employeeDAO;
    private final UserDAO userDAO;
    private final DepartmentDAO departmentDAO;

    public EmployeeServiceImpl() {
        this.employeeDAO = new EmployeeDAOImpl();
        this.userDAO = new UserDAOImpl();
        this.departmentDAO = new DepartmentDAOImpl();
    }

    public EmployeeServiceImpl(EmployeeDAO employeeDAO, UserDAO userDAO, DepartmentDAO departmentDAO) {
        this.employeeDAO = employeeDAO;
        this.userDAO = userDAO;
        this.departmentDAO = departmentDAO;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeDAO.findAll();
    }

    @Override
    public Employee getEmployeeById(Integer employeeId) {
        Employee emp = employeeDAO.findById(employeeId);
        if (emp == null) {
            throw new ResourceNotFoundException("Employee with id " + employeeId + " not found.");
        }
        return emp;
    }

    @Override
    public Employee getEmployeeByUserId(Integer userId) {
        Employee emp = employeeDAO.findByUserId(userId);
        if (emp == null) {
            throw new ResourceNotFoundException("Employee profile not found for user id " + userId + ".");
        }
        return emp;
    }

    @Override
    public List<Employee> getEmployeesByDepartment(Integer departmentId) {
        return employeeDAO.findByDepartmentId(departmentId);
    }

    @Override
    public List<Employee> getEmployeesByManager(Integer managerId) {
        return employeeDAO.findByManagerId(managerId);
    }

    @Override
    public List<User> getAllManagers() {
        return userDAO.findByRole("MANAGER");
    }

    @Override
    public Employee createEmployee(String username, String plainPassword, String fullName, 
                                   String email, String phone, Integer departmentId, 
                                   Integer managerId, String jobTitle, Date hireDate, BigDecimal salary) {
        if (!ValidationUtil.isNotEmpty(username)) {
            throw new ValidationException("Username is required.");
        }
        if (!ValidationUtil.isNotEmpty(plainPassword) || plainPassword.length() < 6) {
            throw new ValidationException("Password is required and must be at least 6 characters.");
        }
        if (!ValidationUtil.isNotEmpty(fullName)) {
            throw new ValidationException("Full name is required.");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new ValidationException("A valid email address is required.");
        }
        if (departmentId == null || departmentDAO.findById(departmentId) == null) {
            throw new ValidationException("A valid department must be selected.");
        }
        if (!ValidationUtil.isNotEmpty(jobTitle)) {
            throw new ValidationException("Job title is required.");
        }
        if (hireDate == null) {
            throw new ValidationException("Hire date is required.");
        }

        if (userDAO.findByUsername(username.trim()) != null) {
            throw new ValidationException("Username '" + username.trim() + "' is already taken.");
        }
        if (userDAO.findByEmail(email.trim()) != null) {
            throw new ValidationException("Email '" + email.trim() + "' is already registered.");
        }

        // 1. Create base User record
        String hash = PasswordUtil.hashPassword(plainPassword);
        Employee employeeUser = new Employee();
        employeeUser.setUsername(username.trim());
        employeeUser.setPasswordHash(hash);
        employeeUser.setRole("EMPLOYEE");
        employeeUser.setFullName(fullName.trim());
        employeeUser.setEmail(email.trim());
        employeeUser.setPhone(phone != null ? phone.trim() : null);
        employeeUser.setStatus("ACTIVE");

        User savedUser = userDAO.save(employeeUser);

        // 2. Create Employee profile record
        employeeUser.setId(savedUser.getId());
        employeeUser.setDepartmentId(departmentId);
        employeeUser.setManagerId(managerId);
        employeeUser.setJobTitle(jobTitle.trim());
        employeeUser.setHireDate(hireDate);
        employeeUser.setSalary(salary != null ? salary : BigDecimal.ZERO);

        return employeeDAO.save(employeeUser);
    }

    @Override
    public void updateEmployee(Integer employeeId, String fullName, String email, String phone, 
                               Integer departmentId, Integer managerId, String jobTitle, 
                               Date hireDate, BigDecimal salary, String status) {
        Employee emp = getEmployeeById(employeeId);

        if (!ValidationUtil.isNotEmpty(fullName)) {
            throw new ValidationException("Full name is required.");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new ValidationException("A valid email address is required.");
        }
        if (departmentId == null || departmentDAO.findById(departmentId) == null) {
            throw new ValidationException("A valid department must be selected.");
        }
        if (!ValidationUtil.isNotEmpty(jobTitle)) {
            throw new ValidationException("Job title is required.");
        }
        if (hireDate == null) {
            throw new ValidationException("Hire date is required.");
        }

        User existingEmail = userDAO.findByEmail(email.trim());
        if (existingEmail != null && !existingEmail.getId().equals(emp.getId())) {
            throw new ValidationException("Email '" + email.trim() + "' is already in use by another account.");
        }

        // Update User table
        emp.setFullName(fullName.trim());
        emp.setEmail(email.trim());
        emp.setPhone(phone != null ? phone.trim() : null);
        emp.setStatus(status != null ? status : "ACTIVE");
        userDAO.update(emp);

        // Update Employee table
        emp.setDepartmentId(departmentId);
        emp.setManagerId(managerId);
        emp.setJobTitle(jobTitle.trim());
        emp.setHireDate(hireDate);
        emp.setSalary(salary != null ? salary : BigDecimal.ZERO);
        employeeDAO.update(emp);
    }

    @Override
    public void assignManager(Integer employeeId, Integer managerId) {
        getEmployeeById(employeeId); // Validate existence
        if (managerId != null && managerId > 0) {
            User manager = userDAO.findById(managerId);
            if (manager == null || !"MANAGER".equalsIgnoreCase(manager.getRole())) {
                throw new ValidationException("Selected user is not an active manager.");
            }
        }
        employeeDAO.assignManager(employeeId, managerId);
    }

    @Override
    public void deleteEmployee(Integer employeeId) {
        getEmployeeById(employeeId); // Validate existence
        employeeDAO.deleteById(employeeId);
    }

    @Override
    public int getTotalEmployeeCount() {
        return employeeDAO.countTotalEmployees();
    }
}
