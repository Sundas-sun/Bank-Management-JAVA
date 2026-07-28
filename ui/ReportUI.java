package ui;

import dao.AuditLogDAO;
import models.AuditLog;
import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ReportUI extends JFrame {
    private User currentUser;
    private AuditLogDAO auditLogDAO;
    private JTable logTable;
    private DefaultTableModel tableModel;
    private JButton refreshBtn;

    public ReportUI(User user) {
        this.currentUser = user;
        this.auditLogDAO = new AuditLogDAO();
        initializeUI();
        loadLogs();
    }

    private void initializeUI() {
        setTitle("Reports & Audit");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "User ID", "Action", "Timestamp"}, 0);
        logTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(logTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadLogs());
        buttonPanel.add(refreshBtn);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadLogs() {
        try {
            List<AuditLog> logs = auditLogDAO.getAllLogs();
            tableModel.setRowCount(0);
            for (AuditLog log : logs) {
                tableModel.addRow(new Object[]{log.getLogId(), log.getUserId(), log.getAction(), log.getTimestamp()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading logs: " + e.getMessage());
        }
    }
}