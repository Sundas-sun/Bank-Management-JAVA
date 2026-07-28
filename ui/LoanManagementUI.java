package ui;

import models.Loan;
import models.User;
import services.LoanService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class LoanManagementUI extends JFrame {
    private User currentUser;
    private LoanService loanService;
    private JTable loanTable;
    private DefaultTableModel tableModel;
    private JButton addBtn, approveBtn, rejectBtn, deleteBtn, refreshBtn;

    public LoanManagementUI(User user) {
        this.currentUser = user;
        this.loanService = new LoanService();
        initializeUI();
        loadLoans();
    }

    private void initializeUI() {
        setTitle("Loan Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Customer ID", "Amount", "Interest Rate", "Status"}, 0);
        loanTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(loanTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        addBtn = new JButton("Add Loan");
        addBtn.addActionListener(new AddLoanAction());
        buttonPanel.add(addBtn);

        approveBtn = new JButton("Approve Loan");
        approveBtn.addActionListener(new ApproveLoanAction());
        buttonPanel.add(approveBtn);

        rejectBtn = new JButton("Reject Loan");
        rejectBtn.addActionListener(new RejectLoanAction());
        buttonPanel.add(rejectBtn);

        deleteBtn = new JButton("Delete Loan");
        deleteBtn.addActionListener(new DeleteLoanAction());
        buttonPanel.add(deleteBtn);

        refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadLoans());
        buttonPanel.add(refreshBtn);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadLoans() {
        try {
            List<Loan> loans = loanService.getAllLoans();
            tableModel.setRowCount(0);
            for (Loan l : loans) {
                tableModel.addRow(new Object[]{l.getLoanId(), l.getCustomerId(), l.getAmount(), l.getInterestRate(), l.getStatus()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading loans: " + e.getMessage());
        }
    }

    private class AddLoanAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField customerIdField = new JTextField();
            JTextField amountField = new JTextField();
            JTextField interestRateField = new JTextField();

            Object[] message = {
                "Customer ID:", customerIdField,
                "Amount:", amountField,
                "Interest Rate:", interestRateField
            };

            int option = JOptionPane.showConfirmDialog(LoanManagementUI.this, message, "Add Loan", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    int customerId = Integer.parseInt(customerIdField.getText());
                    BigDecimal amount = new BigDecimal(amountField.getText());
                    BigDecimal interestRate = new BigDecimal(interestRateField.getText());
                    loanService.applyForLoan(customerId, amount, interestRate);
                    loadLoans();
                    JOptionPane.showMessageDialog(LoanManagementUI.this, "Loan application submitted successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoanManagementUI.this, "Error: " + ex.getMessage());
                }
            }
        }
    }

    private class ApproveLoanAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = loanTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(LoanManagementUI.this, "Please select a loan to approve.");
                return;
            }
            int loanId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                loanService.approveLoan(loanId, currentUser);
                loadLoans();
                JOptionPane.showMessageDialog(LoanManagementUI.this, "Loan approved successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(LoanManagementUI.this, "Error: " + ex.getMessage());
            }
        }
    }

    private class RejectLoanAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = loanTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(LoanManagementUI.this, "Please select a loan to reject.");
                return;
            }
            int loanId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                loanService.rejectLoan(loanId, currentUser);
                loadLoans();
                JOptionPane.showMessageDialog(LoanManagementUI.this, "Loan rejected successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(LoanManagementUI.this, "Error: " + ex.getMessage());
            }
        }
    }

    private class DeleteLoanAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = loanTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(LoanManagementUI.this, "Please select a loan to delete.");
                return;
            }
            int loanId = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(
                LoanManagementUI.this,
                "Are you sure you want to permanently delete this loan?",
                "Delete Loan",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            try {
                loanService.deleteLoan(loanId);
                loadLoans();
                JOptionPane.showMessageDialog(LoanManagementUI.this, "Loan deleted successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(LoanManagementUI.this, "Error: " + ex.getMessage());
            }
        }
    }
}