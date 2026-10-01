package com.eps.dao.impl;

import com.eps.dao.EvaluationCriterionDAO;
import com.eps.exception.DatabaseException;
import com.eps.model.EvaluationCriterion;
import com.eps.util.DBConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of EvaluationCriterionDAO.
 */
public class EvaluationCriterionDAOImpl implements EvaluationCriterionDAO {

    private final DBConnectionManager dbManager = DBConnectionManager.getInstance();

    private EvaluationCriterion mapCriterion(ResultSet rs) throws SQLException {
        EvaluationCriterion ec = new EvaluationCriterion();
        ec.setId(rs.getInt("id"));
        ec.setName(rs.getString("name"));
        ec.setDescription(rs.getString("description"));
        ec.setWeight(rs.getBigDecimal("weight"));
        ec.setActive(rs.getBoolean("is_active"));
        ec.setCreatedAt(rs.getTimestamp("created_at"));
        return ec;
    }

    @Override
    public EvaluationCriterion findById(Integer id) {
        String sql = "SELECT * FROM evaluation_criteria WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCriterion(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find evaluation criterion by id: " + id, e);
        }
        return null;
    }

    @Override
    public List<EvaluationCriterion> findAll() {
        List<EvaluationCriterion> list = new ArrayList<>();
        String sql = "SELECT * FROM evaluation_criteria ORDER BY id ASC";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapCriterion(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to list evaluation criteria", e);
        }
        return list;
    }

    @Override
    public List<EvaluationCriterion> findAllActive() {
        List<EvaluationCriterion> list = new ArrayList<>();
        String sql = "SELECT * FROM evaluation_criteria WHERE is_active = TRUE ORDER BY id ASC";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapCriterion(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to list active evaluation criteria", e);
        }
        return list;
    }

    @Override
    public BigDecimal calculateTotalActiveWeight() {
        String sql = "SELECT SUM(weight) FROM evaluation_criteria WHERE is_active = TRUE";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                BigDecimal sum = rs.getBigDecimal(1);
                return sum != null ? sum : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to calculate total criteria weight", e);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public EvaluationCriterion save(EvaluationCriterion criterion) {
        String sql = "INSERT INTO evaluation_criteria (name, description, weight, is_active) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, criterion.getName());
            ps.setString(2, criterion.getDescription());
            ps.setBigDecimal(3, criterion.getWeight());
            ps.setBoolean(4, criterion.isActive());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        criterion.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return criterion;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert evaluation criterion: " + criterion.getName(), e);
        }
    }

    @Override
    public boolean update(EvaluationCriterion criterion) {
        String sql = "UPDATE evaluation_criteria SET name = ?, description = ?, weight = ?, is_active = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, criterion.getName());
            ps.setString(2, criterion.getDescription());
            ps.setBigDecimal(3, criterion.getWeight());
            ps.setBoolean(4, criterion.isActive());
            ps.setInt(5, criterion.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update evaluation criterion id: " + criterion.getId(), e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM evaluation_criteria WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete evaluation criterion id: " + id, e);
        }
    }
}
