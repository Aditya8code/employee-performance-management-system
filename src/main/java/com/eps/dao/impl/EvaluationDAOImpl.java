package com.eps.dao.impl;

import com.eps.dao.EvaluationDAO;
import com.eps.exception.DatabaseException;
import com.eps.model.DepartmentStats;
import com.eps.model.Evaluation;
import com.eps.model.EvaluationScore;
import com.eps.model.ReportFilter;
import com.eps.util.DBConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of EvaluationDAO.
 * Handles appraisals, scores, and organizational performance aggregations.
 */
public class EvaluationDAOImpl implements EvaluationDAO {

    private final DBConnectionManager dbManager = DBConnectionManager.getInstance();

    private Evaluation mapEvaluationHeader(ResultSet rs) throws SQLException {
        Evaluation eval = new Evaluation();
        eval.setId(rs.getInt("id"));
        eval.setEmployeeId(rs.getInt("employee_id"));
        eval.setEmployeeName(rs.getString("employee_name"));
        eval.setEmployeeEmail(rs.getString("employee_email"));
        eval.setEmployeeTitle(rs.getString("job_title"));
        eval.setDepartmentName(rs.getString("department_name"));

        eval.setManagerId(rs.getInt("manager_id"));
        eval.setManagerName(rs.getString("manager_name"));

        eval.setCycleId(rs.getInt("cycle_id"));
        eval.setCycleName(rs.getString("cycle_name"));
        try {
            eval.setCycleStatus(rs.getString("cycle_status"));
        } catch (SQLException ignored) {
        }

        BigDecimal total = rs.getBigDecimal("total_score");
        eval.setTotalScore(total);
        eval.setRatingLabel(rs.getString("rating_label"));
        eval.setOverallFeedback(rs.getString("overall_feedback"));
        eval.setStrengths(rs.getString("strengths"));
        eval.setImprovements(rs.getString("improvements"));
        eval.setStatus(rs.getString("status"));
        eval.setSubmittedAt(rs.getTimestamp("submitted_at"));
        eval.setCreatedAt(rs.getTimestamp("created_at"));
        eval.setUpdatedAt(rs.getTimestamp("updated_at"));

        return eval;
    }

    private EvaluationScore mapEvaluationScore(ResultSet rs) throws SQLException {
        EvaluationScore es = new EvaluationScore();
        es.setId(rs.getInt("id"));
        es.setEvaluationId(rs.getInt("evaluation_id"));
        es.setCriterionId(rs.getInt("criterion_id"));
        es.setCriterionName(rs.getString("criterion_name"));
        es.setCriterionDescription(rs.getString("criterion_description"));
        es.setCriterionWeight(rs.getBigDecimal("criterion_weight"));
        es.setScore(rs.getInt("score"));
        es.setComments(rs.getString("comments"));
        return es;
    }

    private String getBaseSelectQuery() {
        return "SELECT ev.*, " +
               "ue.full_name AS employee_name, ue.email AS employee_email, emp.job_title, " +
               "dept.name AS department_name, " +
               "um.full_name AS manager_name, " +
               "cyc.name AS cycle_name, cyc.status AS cycle_status " +
               "FROM evaluations ev " +
               "JOIN employees emp ON ev.employee_id = emp.id " +
               "JOIN users ue ON emp.user_id = ue.id " +
               "JOIN departments dept ON emp.department_id = dept.id " +
               "JOIN users um ON ev.manager_id = um.id " +
               "JOIN evaluation_cycles cyc ON ev.cycle_id = cyc.id ";
    }

    @Override
    public Evaluation findById(Integer id) {
        String sql = getBaseSelectQuery() + "WHERE ev.id = ?";
        Evaluation eval = null;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    eval = mapEvaluationHeader(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find evaluation by id: " + id, e);
        }

        if (eval != null) {
            eval.setScores(findScoresByEvaluationId(eval.getId()));
        }
        return eval;
    }

    @Override
    public List<Evaluation> findAll() {
        List<Evaluation> list = new ArrayList<>();
        String sql = getBaseSelectQuery() + "ORDER BY ev.updated_at DESC";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapEvaluationHeader(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to list all evaluations", e);
        }
        return list;
    }

    @Override
    public Evaluation findByEmployeeAndCycle(Integer employeeId, Integer cycleId) {
        String sql = getBaseSelectQuery() + "WHERE ev.employee_id = ? AND ev.cycle_id = ?";
        Evaluation eval = null;
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ps.setInt(2, cycleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    eval = mapEvaluationHeader(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find evaluation by employee and cycle", e);
        }

        if (eval != null) {
            eval.setScores(findScoresByEvaluationId(eval.getId()));
        }
        return eval;
    }

    @Override
    public List<Evaluation> findByEmployeeId(Integer employeeId) {
        List<Evaluation> list = new ArrayList<>();
        String sql = getBaseSelectQuery() + "WHERE ev.employee_id = ? ORDER BY cyc.start_date DESC";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Evaluation eval = mapEvaluationHeader(rs);
                    list.add(eval);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to list evaluations for employee: " + employeeId, e);
        }

        for (Evaluation eval : list) {
            eval.setScores(findScoresByEvaluationId(eval.getId()));
        }
        return list;
    }

    @Override
    public List<Evaluation> findByManagerAndCycle(Integer managerId, Integer cycleId) {
        List<Evaluation> list = new ArrayList<>();
        String sql = getBaseSelectQuery() + "WHERE ev.manager_id = ? AND ev.cycle_id = ? ORDER BY ue.full_name ASC";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, managerId);
            ps.setInt(2, cycleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapEvaluationHeader(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to list evaluations for manager in cycle", e);
        }
        return list;
    }

    @Override
    public List<Evaluation> findByCycleId(Integer cycleId) {
        List<Evaluation> list = new ArrayList<>();
        String sql = getBaseSelectQuery() + "WHERE ev.cycle_id = ? ORDER BY dept.name ASC, ue.full_name ASC";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cycleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapEvaluationHeader(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to list evaluations for cycle: " + cycleId, e);
        }
        return list;
    }

    @Override
    public List<Evaluation> searchEvaluations(ReportFilter filter) {
        List<Evaluation> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(getBaseSelectQuery()).append("WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (filter.getCycleId() != null && filter.getCycleId() > 0) {
            sql.append("AND ev.cycle_id = ? ");
            params.add(filter.getCycleId());
        }
        if (filter.getDepartmentId() != null && filter.getDepartmentId() > 0) {
            sql.append("AND emp.department_id = ? ");
            params.add(filter.getDepartmentId());
        }
        if (filter.getRatingLabel() != null && !filter.getRatingLabel().trim().isEmpty() && !"ALL".equalsIgnoreCase(filter.getRatingLabel())) {
            sql.append("AND ev.rating_label = ? ");
            params.add(filter.getRatingLabel().trim());
        }
        if (filter.getStatus() != null && !filter.getStatus().trim().isEmpty() && !"ALL".equalsIgnoreCase(filter.getStatus())) {
            sql.append("AND ev.status = ? ");
            params.add(filter.getStatus().trim());
        }

        sql.append("ORDER BY dept.name ASC, ue.full_name ASC");

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapEvaluationHeader(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to search evaluations with filters", e);
        }
        return list;
    }

    @Override
    public List<EvaluationScore> findScoresByEvaluationId(Integer evaluationId) {
        List<EvaluationScore> scores = new ArrayList<>();
        String sql = "SELECT es.*, ec.name AS criterion_name, ec.description AS criterion_description, " +
                     "ec.weight AS criterion_weight " +
                     "FROM evaluation_scores es " +
                     "JOIN evaluation_criteria ec ON es.criterion_id = ec.id " +
                     "WHERE es.evaluation_id = ? " +
                     "ORDER BY ec.id ASC";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, evaluationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    scores.add(mapEvaluationScore(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find scores for evaluation: " + evaluationId, e);
        }
        return scores;
    }

    @Override
    public Evaluation save(Evaluation eval) {
        String sql = "INSERT INTO evaluations (employee_id, manager_id, cycle_id, total_score, " +
                     "rating_label, overall_feedback, strengths, improvements, status, submitted_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "manager_id = VALUES(manager_id), " +
                     "total_score = VALUES(total_score), " +
                     "rating_label = VALUES(rating_label), " +
                     "overall_feedback = VALUES(overall_feedback), " +
                     "strengths = VALUES(strengths), " +
                     "improvements = VALUES(improvements), " +
                     "status = VALUES(status), " +
                     "submitted_at = VALUES(submitted_at)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, eval.getEmployeeId());
            ps.setInt(2, eval.getManagerId());
            ps.setInt(3, eval.getCycleId());

            if (eval.getTotalScore() != null) {
                ps.setBigDecimal(4, eval.getTotalScore());
            } else {
                ps.setNull(4, Types.DECIMAL);
            }

            ps.setString(5, eval.getRatingLabel());
            ps.setString(6, eval.getOverallFeedback());
            ps.setString(7, eval.getStrengths());
            ps.setString(8, eval.getImprovements());
            ps.setString(9, eval.getStatus() != null ? eval.getStatus() : "DRAFT");

            if (eval.getSubmittedAt() != null) {
                ps.setTimestamp(10, eval.getSubmittedAt());
            } else {
                ps.setNull(10, Types.TIMESTAMP);
            }

            int affected = ps.executeUpdate();
            if (eval.getId() == null || eval.getId() <= 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        eval.setId(generatedKeys.getInt(1));
                    }
                }
                // If ON DUPLICATE KEY UPDATE didn't yield generated key, query by unique constraint
                if (eval.getId() == null || eval.getId() <= 0) {
                    Evaluation existing = findByEmployeeAndCycle(eval.getEmployeeId(), eval.getCycleId());
                    if (existing != null) {
                        eval.setId(existing.getId());
                    }
                }
            }

            if (eval.getScores() != null && !eval.getScores().isEmpty() && eval.getId() != null) {
                saveScores(eval.getId(), eval.getScores());
            }

            return eval;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save evaluation", e);
        }
    }

    @Override
    public boolean update(Evaluation eval) {
        String sql = "UPDATE evaluations SET manager_id = ?, total_score = ?, rating_label = ?, " +
                     "overall_feedback = ?, strengths = ?, improvements = ?, status = ?, submitted_at = ? " +
                     "WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, eval.getManagerId());
            if (eval.getTotalScore() != null) {
                ps.setBigDecimal(2, eval.getTotalScore());
            } else {
                ps.setNull(2, Types.DECIMAL);
            }
            ps.setString(3, eval.getRatingLabel());
            ps.setString(4, eval.getOverallFeedback());
            ps.setString(5, eval.getStrengths());
            ps.setString(6, eval.getImprovements());
            ps.setString(7, eval.getStatus());
            if (eval.getSubmittedAt() != null) {
                ps.setTimestamp(8, eval.getSubmittedAt());
            } else {
                ps.setNull(8, Types.TIMESTAMP);
            }
            ps.setInt(9, eval.getId());

            boolean ok = ps.executeUpdate() > 0;
            if (ok && eval.getScores() != null && !eval.getScores().isEmpty()) {
                saveScores(eval.getId(), eval.getScores());
            }
            return ok;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update evaluation id: " + eval.getId(), e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM evaluations WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete evaluation id: " + id, e);
        }
    }

    @Override
    public boolean saveScores(Integer evaluationId, List<EvaluationScore> scores) {
        String sql = "INSERT INTO evaluation_scores (evaluation_id, criterion_id, score, comments) " +
                     "VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE score = VALUES(score), comments = VALUES(comments)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (EvaluationScore es : scores) {
                ps.setInt(1, evaluationId);
                ps.setInt(2, es.getCriterionId());
                ps.setInt(3, es.getScore());
                ps.setString(4, es.getComments());
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save evaluation scores for evaluation: " + evaluationId, e);
        }
    }

    @Override
    public List<DepartmentStats> getDepartmentPerformanceStats(Integer cycleId) {
        List<DepartmentStats> statsList = new ArrayList<>();
        String sql = "SELECT d.id AS dept_id, d.name AS dept_name, " +
                     "COUNT(DISTINCT emp.id) AS total_employees, " +
                     "COUNT(DISTINCT CASE WHEN ev.status = 'SUBMITTED' OR ev.status = 'APPROVED' THEN ev.id END) AS completed_evals, " +
                     "COUNT(DISTINCT CASE WHEN ev.status = 'DRAFT' OR ev.id IS NULL THEN emp.id END) AS pending_evals, " +
                     "AVG(CASE WHEN ev.status = 'SUBMITTED' OR ev.status = 'APPROVED' THEN ev.total_score END) AS avg_score, " +
                     "COUNT(CASE WHEN (ev.status = 'SUBMITTED' OR ev.status = 'APPROVED') AND ev.total_score >= 4.50 THEN 1 END) AS count_outstanding, " +
                     "COUNT(CASE WHEN (ev.status = 'SUBMITTED' OR ev.status = 'APPROVED') AND ev.total_score >= 3.50 AND ev.total_score < 4.50 THEN 1 END) AS count_exceeds, " +
                     "COUNT(CASE WHEN (ev.status = 'SUBMITTED' OR ev.status = 'APPROVED') AND ev.total_score >= 2.50 AND ev.total_score < 3.50 THEN 1 END) AS count_meets, " +
                     "COUNT(CASE WHEN (ev.status = 'SUBMITTED' OR ev.status = 'APPROVED') AND ev.total_score >= 1.50 AND ev.total_score < 2.50 THEN 1 END) AS count_needs, " +
                     "COUNT(CASE WHEN (ev.status = 'SUBMITTED' OR ev.status = 'APPROVED') AND ev.total_score < 1.50 THEN 1 END) AS count_unsatisfactory " +
                     "FROM departments d " +
                     "LEFT JOIN employees emp ON emp.department_id = d.id " +
                     "LEFT JOIN evaluations ev ON ev.employee_id = emp.id " + (cycleId != null ? "AND ev.cycle_id = ? " : "") +
                     "GROUP BY d.id, d.name " +
                     "ORDER BY d.name ASC";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (cycleId != null) {
                ps.setInt(1, cycleId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DepartmentStats ds = new DepartmentStats();
                    ds.setDepartmentId(rs.getInt("dept_id"));
                    ds.setDepartmentName(rs.getString("dept_name"));
                    ds.setTotalEmployees(rs.getInt("total_employees"));
                    ds.setCompletedEvaluations(rs.getInt("completed_evals"));
                    ds.setPendingEvaluations(rs.getInt("pending_evals"));

                    BigDecimal avg = rs.getBigDecimal("avg_score");
                    ds.setAverageScore(avg != null ? avg.setScale(2, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO);

                    ds.setOutstandingCount(rs.getInt("count_outstanding"));
                    ds.setExceedsCount(rs.getInt("count_exceeds"));
                    ds.setMeetsCount(rs.getInt("count_meets"));
                    ds.setNeedsImprovementCount(rs.getInt("count_needs"));
                    ds.setUnsatisfactoryCount(rs.getInt("count_unsatisfactory"));

                    statsList.add(ds);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to aggregate department performance stats", e);
        }
        return statsList;
    }

    @Override
    public int countEvaluationsByStatus(Integer cycleId, String status) {
        String sql = "SELECT COUNT(*) FROM evaluations WHERE cycle_id = ? AND status = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cycleId);
            ps.setString(2, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to count evaluations by status", e);
        }
        return 0;
    }

    @Override
    public Double getAverageCompanyScore(Integer cycleId) {
        String sql = "SELECT AVG(total_score) FROM evaluations WHERE cycle_id = ? AND (status = 'SUBMITTED' OR status = 'APPROVED')";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cycleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double val = rs.getDouble(1);
                    return rs.wasNull() ? null : val;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to calculate company average score", e);
        }
        return null;
    }
}
