package ui;

import models.Account;
import models.User;
import services.AccountService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class AccountManagementUI extends JFrame {
    private User currentUser;
    private AccountService accountService;
    private JTable accountTable;
    private DefaultTableModel tableModel;
    private JButton addBtn, freezeBtn, closeBtn, deleteBtn, refreshBtn;

    public AccountManagementUI(User user) {
        this.currentUser = user;
        this.accountService = new AccountService();
        initializeUI();
        loadAccounts();
    }

    private void initializeUI() {
        setTitle("Account Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Customer ID", "Type", "Balance", "Status"}, 0);
        accountTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(accountTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        addBtn = new JButton("Add Account");
        addBtn.addActionListener(new AddAccountAction());
        buttonPanel.add(addBtn);

        freezeBtn = new JButton("Freeze Account");
        freezeBtn.addActionListener(new FreezeAccountAction());
        buttonPanel.add(freezeBtn);

        closeBtn = new JButton("Close Account");
        closeBtn.addActionListener(new CloseAccountAction());
        buttonPanel.add(closeBtn);

        deleteBtn = new JButton("Delete Account");
        deleteBtn.addActionListener(new DeleteAccountAction());
        buttonPanel.add(deleteBtn);

        refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadAccounts());
        buttonPanel.add(refreshBtn);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadAccounts() {
        try {
            List<Account> accounts = accountService.getAllAccounts();
            tableModel.setRowCount(0);
            for (Account a : accounts) {
                tableModel.addRow(new Object[]{a.getAccountId(), a.getCustomerId(), a.getAccountType(), a.getBalance(), a.getStatus()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading accounts: " + e.getMessage());
        }
    }

    private class AddAccountAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField customerIdField = new JTextField();
            JTextField typeField = new JTextField();
            JTextField balanceField = new JTextField();

            Object[] message = {
                "Customer ID:", customerIdField,
                "Account Type:", typeField,
                "Initial Balance:", balanceField
            };

            int option = JOptionPane.showConfirmDialog(AccountManagementUI.this, message, "Add Account", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    int customerId = Integer.parseInt(customerIdField.getText());
                    String type = typeField.getText();
                    BigDecimal balance = new BigDecimal(balanceField.getText());
                    accountService.createAccount(customerId, type, balance);
                    loadAccounts();
                    JOptionPane.showMessageDialog(AccountManagementUI.this, "Account added successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AccountManagementUI.this, "Error: " + ex.getMessage());
                }
            }
        }
    }

    private class FreezeAccountAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = accountTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(AccountManagementUI.this, "Please select an account to freeze.");
                return;
            }
            int accountId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                accountService.freezeAccount(accountId);
                loadAccounts();
                JOptionPane.showMessageDialog(AccountManagementUI.this, "Account frozen successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(AccountManagementUI.this, "Error: " + ex.getMessage());
            }
        }
    }

    private class CloseAccountAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = accountTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(AccountManagementUI.this, "Please select an account to close.");
                return;
            }
            int accountId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                accountService.closeAccount(accountId);
                loadAccounts();
                JOptionPane.showMessageDialog(AccountManagementUI.this, "Account closed successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(AccountManagementUI.this, "Error: " + ex.getMessage());
            }
        }
    }

    private class DeleteAccountAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = accountTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(AccountManagementUI.this, "Please select an account to delete.");
                return;
            }
            int accountId = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(
                AccountManagementUI.this,
                "Are you sure you want to permanently delete this account?",
                "Delete Account",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            try {
                accountService.deleteAccount(accountId);
                loadAccounts();
                JOptionPane.showMessageDialog(AccountManagementUI.this, "Account deleted successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(AccountManagementUI.this, "Error: " + ex.getMessage());
            }
        }
    }
}