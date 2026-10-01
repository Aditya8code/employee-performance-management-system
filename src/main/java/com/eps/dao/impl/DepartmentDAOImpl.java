package com.eps.dao.impl;

import com.eps.dao.DepartmentDAO;
import com.eps.exception.DatabaseException;
import com.eps.model.Department;
import com.eps.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of DepartmentDAO.
 */
public class DepartmentDAOImpl implements DepartmentDAO {

    private final DBConnectionManager dbManager = DBConnectionManager.getInstance();

    private Department mapDepartment(ResultSet rs) throws SQLException {
        Department dept = new Department();
        dept.setId(rs.getInt("id"));
        dept.setName(rs.getString("name"));
        dept.setDescription(rs.getString("description"));
        dept.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            dept.setEmployeeCount(rs.getInt("emp_count"));
        } catch (SQLException ignored) {
            // emp_count not in projection
        }
        return dept;
    }

    @Override
    public Department findById(Integer id) {
        String sql = "SELECT d.*, COUNT(e.id) AS emp_count " +
                     "FROM departments d " +
                     "LEFT JOIN employees e ON e.department_id = d.id " +
                     "WHERE d.id = ? " +
                     "GROUP BY d.id, d.name, d.description, d.created_at";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapDepartment(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find department by id: " + id, e);
        }
        return null;
    }

    @Override
    public List<Department> findAll() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT d.*, COUNT(e.id) AS emp_count " +
                     "FROM departments d " +
                     "LEFT JOIN employees e ON e.department_id = d.id " +
                     "GROUP BY d.id, d.name, d.description, d.created_at " +
                     "ORDER BY d.name ASC";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapDepartment(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to list departments", e);
        }
        return list;
    }

    @Override
    public Department save(Department dept) {
        String sql = "INSERT INTO departments (name, description) VALUES (?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, dept.getName());
            ps.setString(2, dept.getDescription());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        dept.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return dept;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert department: " + dept.getName(), e);
        }
    }

    @Override
    public boolean update(Department dept) {
        String sql = "UPDATE departments SET name = ?, description = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dept.getName());
            ps.setString(2, dept.getDescription());
            ps.setInt(3, dept.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update department id: " + dept.getId(), e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM departments WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete department id: " + id, e);
        }
    }

    @Override
    public Department findByName(String name) {
        String sql = "SELECT * FROM departments WHERE name = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapDepartment(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find department by name: " + name, e);
        }
        return null;
    }

    @Override
    public int countEmployeesInDepartment(Integer departmentId) {
        String sql = "SELECT COUNT(*) FROM employees WHERE department_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to count employees in department: " + departmentId, e);
        }
        return 0;
    }
}
