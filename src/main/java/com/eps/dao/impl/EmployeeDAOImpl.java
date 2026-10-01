package com.eps.dao.impl;

import com.eps.dao.EmployeeDAO;
import com.eps.exception.DatabaseException;
import com.eps.model.Employee;
import com.eps.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of EmployeeDAO.
 * Joins users, employees, departments, and manager information.
 */
public class EmployeeDAOImpl implements EmployeeDAO {

    private final DBConnectionManager dbManager = DBConnectionManager.getInstance();

    private Employee mapEmployee(ResultSet rs) throws SQLException {
        Employee emp = new Employee();

        // User base fields
        emp.setId(rs.getInt("user_id"));
        emp.setUsername(rs.getString("username"));
        emp.setPasswordHash(rs.getString("password_hash"));
        emp.setRole(rs.getString("role"));
        emp.setFullName(rs.getString("full_name"));
        emp.setEmail(rs.getString("email"));
        emp.setPhone(rs.getString("phone"));
        emp.setStatus(rs.getString("status"));
        emp.setCreatedAt(rs.getTimestamp("created_at"));
        emp.setUpdatedAt(rs.getTimestamp("updated_at"));

        // Employee specific fields
        emp.setEmployeeId(rs.getInt("employee_id"));
        emp.setDepartmentId(rs.getInt("department_id"));
        emp.setDepartmentName(rs.getString("department_name"));

        int mgrId = rs.getInt("manager_id");
        if (!rs.wasNull()) {
            emp.setManagerId(mgrId);
            emp.setManagerName(rs.getString("manager_name"));
        }

        emp.setJobTitle(rs.getString("job_title"));
        emp.setHireDate(rs.getDate("hire_date"));
        emp.setSalary(rs.getBigDecimal("salary"));

        return emp;
    }

    private String getBaseSelectQuery() {
        return "SELECT e.id AS employee_id, e.department_id, d.name AS department_name, " +
               "e.manager_id, m.full_name AS manager_name, e.job_title, e.hire_date, e.salary, " +
               "u.id AS user_id, u.username, u.password_hash, u.role, u.full_name, u.email, " +
               "u.phone, u.status, u.created_at, u.updated_at " +
               "FROM employees e " +
               "JOIN users u ON e.user_id = u.id " +
               "JOIN departments d ON e.department_id = d.id " +
               "LEFT JOIN users m ON e.manager_id = m.id ";
    }

    @Override
    public Employee findById(Integer employeeId) {
        String sql = getBaseSelectQuery() + "WHERE e.id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapEmployee(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find employee by id: " + employeeId, e);
        }
        return null;
    }

    @Override
    public Employee findByUserId(Integer userId) {
        String sql = getBaseSelectQuery() + "WHERE e.user_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapEmployee(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find employee by user id: " + userId, e);
        }
        return null;
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> list = new ArrayList<>();
        String sql = getBaseSelectQuery() + "ORDER BY u.full_name ASC";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapEmployee(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to list all employees", e);
        }
        return list;
    }

    @Override
    public List<Employee> findByDepartmentId(Integer departmentId) {
        List<Employee> list = new ArrayList<>();
        String sql = getBaseSelectQuery() + "WHERE e.department_id = ? ORDER BY u.full_name ASC";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapEmployee(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to list employees by department: " + departmentId, e);
        }
        return list;
    }

    @Override
    public List<Employee> findByManagerId(Integer managerId) {
        List<Employee> list = new ArrayList<>();
        String sql = getBaseSelectQuery() + "WHERE e.manager_id = ? ORDER BY u.full_name ASC";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, managerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapEmployee(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to list employees by manager: " + managerId, e);
        }
        return list;
    }

    @Override
    public Employee save(Employee employee) {
        String sql = "INSERT INTO employees (user_id, department_id, manager_id, job_title, hire_date, salary) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, employee.getId()); // User ID
            ps.setInt(2, employee.getDepartmentId());
            if (employee.getManagerId() != null) {
                ps.setInt(3, employee.getManagerId());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.setString(4, employee.getJobTitle());
            ps.setDate(5, employee.getHireDate());
            ps.setBigDecimal(6, employee.getSalary());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        employee.setEmployeeId(generatedKeys.getInt(1));
                    }
                }
            }
            return employee;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save employee profile", e);
        }
    }

    @Override
    public boolean update(Employee employee) {
        String sql = "UPDATE employees SET department_id = ?, manager_id = ?, job_title = ?, " +
                     "hire_date = ?, salary = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employee.getDepartmentId());
            if (employee.getManagerId() != null) {
                ps.setInt(2, employee.getManagerId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setString(3, employee.getJobTitle());
            ps.setDate(4, employee.getHireDate());
            ps.setBigDecimal(5, employee.getSalary());
            ps.setInt(6, employee.getEmployeeId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update employee id: " + employee.getEmployeeId(), e);
        }
    }

    @Override
    public boolean assignManager(Integer employeeId, Integer managerId) {
        String sql = "UPDATE employees SET manager_id = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (managerId != null) {
                ps.setInt(1, managerId);
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setInt(2, employeeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to assign manager to employee: " + employeeId, e);
        }
    }

    @Override
    public boolean deleteById(Integer employeeId) {
        // Find user_id first to delete corresponding user account (which cascades)
        String findUserSql = "SELECT user_id FROM employees WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement psFind = conn.prepareStatement(findUserSql)) {
            psFind.setInt(1, employeeId);
            try (ResultSet rs = psFind.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String delUserSql = "DELETE FROM users WHERE id = ?";
                    try (PreparedStatement psDel = conn.prepareStatement(delUserSql)) {
                        psDel.setInt(1, userId);
                        return psDel.executeUpdate() > 0;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete employee id: " + employeeId, e);
        }
        return false;
    }

    @Override
    public int countTotalEmployees() {
        String sql = "SELECT COUNT(*) FROM employees";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to count total employees", e);
        }
        return 0;
    }
}
