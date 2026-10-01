package com.eps.dao.impl;

import com.eps.dao.EvaluationCycleDAO;
import com.eps.exception.DatabaseException;
import com.eps.model.EvaluationCycle;
import com.eps.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of EvaluationCycleDAO.
 */
public class EvaluationCycleDAOImpl implements EvaluationCycleDAO {

    private final DBConnectionManager dbManager = DBConnectionManager.getInstance();

    private EvaluationCycle mapCycle(ResultSet rs) throws SQLException {
        EvaluationCycle cycle = new EvaluationCycle();
        cycle.setId(rs.getInt("id"));
        cycle.setName(rs.getString("name"));
        cycle.setDescription(rs.getString("description"));
        cycle.setStartDate(rs.getDate("start_date"));
        cycle.setEndDate(rs.getDate("end_date"));
        cycle.setStatus(rs.getString("status"));
        cycle.setCreatedAt(rs.getTimestamp("created_at"));
        cycle.setUpdatedAt(rs.getTimestamp("updated_at"));

        try {
            cycle.setTotalEvaluations(rs.getInt("total_evals"));
            cycle.setCompletedEvaluations(rs.getInt("completed_evals"));
        } catch (SQLException ignored) {
            // Count projections may be absent
        }

        return cycle;
    }

    private String getBaseSelectQuery() {
        return "SELECT c.*, " +
               "(SELECT COUNT(*) FROM evaluations ev WHERE ev.cycle_id = c.id) AS total_evals, " +
               "(SELECT COUNT(*) FROM evaluations ev WHERE ev.cycle_id = c.id AND (ev.status = 'SUBMITTED' OR ev.status = 'APPROVED')) AS completed_evals " +
               "FROM evaluation_cycles c ";
    }

    @Override
    public EvaluationCycle findById(Integer id) {
        String sql = getBaseSelectQuery() + "WHERE c.id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCycle(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find evaluation cycle by id: " + id, e);
        }
        return null;
    }

    @Override
    public List<EvaluationCycle> findAll() {
        List<EvaluationCycle> list = new ArrayList<>();
        String sql = getBaseSelectQuery() + "ORDER BY c.start_date DESC";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapCycle(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to list evaluation cycles", e);
        }
        return list;
    }

    @Override
    public EvaluationCycle findActiveCycle() {
        String sql = getBaseSelectQuery() + "WHERE c.status = 'ACTIVE' ORDER BY c.start_date DESC LIMIT 1";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapCycle(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find active evaluation cycle", e);
        }
        return null;
    }

    @Override
    public List<EvaluationCycle> findRecentCycles(int limit) {
        List<EvaluationCycle> list = new ArrayList<>();
        String sql = getBaseSelectQuery() + "ORDER BY c.start_date DESC LIMIT ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCycle(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch recent evaluation cycles", e);
        }
        return list;
    }

    @Override
    public EvaluationCycle save(EvaluationCycle cycle) {
        String sql = "INSERT INTO evaluation_cycles (name, description, start_date, end_date, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cycle.getName());
            ps.setString(2, cycle.getDescription());
            ps.setDate(3, cycle.getStartDate());
            ps.setDate(4, cycle.getEndDate());
            ps.setString(5, cycle.getStatus() != null ? cycle.getStatus() : "DRAFT");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cycle.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return cycle;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert evaluation cycle: " + cycle.getName(), e);
        }
    }

    @Override
    public boolean update(EvaluationCycle cycle) {
        String sql = "UPDATE evaluation_cycles SET name = ?, description = ?, start_date = ?, " +
                     "end_date = ?, status = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cycle.getName());
            ps.setString(2, cycle.getDescription());
            ps.setDate(3, cycle.getStartDate());
            ps.setDate(4, cycle.getEndDate());
            ps.setString(5, cycle.getStatus());
            ps.setInt(6, cycle.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update evaluation cycle id: " + cycle.getId(), e);
        }
    }

    @Override
    public boolean updateStatus(Integer cycleId, String status) {
        String sql = "UPDATE evaluation_cycles SET status = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, cycleId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update cycle status for id: " + cycleId, e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM evaluation_cycles WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete evaluation cycle id: " + id, e);
        }
    }
}
