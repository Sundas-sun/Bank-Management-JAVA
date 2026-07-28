package models;

import java.sql.Timestamp;

public class AuditLog {
    private int logId;
    private Integer userId; // Nullable if system action
    private String action;
    private Timestamp timestamp;

    public AuditLog() {}

    public AuditLog(int logId, Integer userId, String action, Timestamp timestamp) {
        this.logId = logId;
        this.userId = userId;
        this.action = action;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}