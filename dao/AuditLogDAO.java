package dao;

import models.AuditLog;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDAO {
    public void logAction(AuditLog log) throws SQLException {
        String sql = "INSERT INTO audit_logs (user_id, action) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (log.getUserId() != null) {
                stmt.setInt(1, log.getUserId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, log.getAction());
            stmt.executeUpdate();
        }
    }

    public void logAction(Connection conn, AuditLog log) throws SQLException {
        String sql = "INSERT INTO audit_logs (user_id, action) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (log.getUserId() != null) {
                stmt.setInt(1, log.getUserId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, log.getAction());
            stmt.executeUpdate();
        }
    }

    public List<AuditLog> getAllLogs() throws SQLException {
        List<AuditLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM audit_logs ORDER BY timestamp DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                logs.add(new AuditLog(
                    rs.getInt("log_id"),
                    rs.getObject("user_id", Integer.class),
                    rs.getString("action"),
                    rs.getTimestamp("timestamp")
                ));
            }
        }
        return logs;
    }
}