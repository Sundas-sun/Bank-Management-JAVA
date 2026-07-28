package ui;

import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardUI extends JFrame {
    private User currentUser;
    private JButton customerBtn, accountBtn, transactionBtn, loanBtn, reportBtn, logoutBtn;

    public DashboardUI(User user) {
        this.currentUser = user;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Dashboard - " + currentUser.getRole());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        customerBtn = new JButton("Customer Management");
        customerBtn.addActionListener(e -> new CustomerManagementUI(currentUser).setVisible(true));
        panel.add(customerBtn);

        accountBtn = new JButton("Account Management");
        accountBtn.addActionListener(e -> new AccountManagementUI(currentUser).setVisible(true));
        panel.add(accountBtn);

        transactionBtn = new JButton("Transactions");
        transactionBtn.addActionListener(e -> new TransactionUI(currentUser).setVisible(true));
        panel.add(transactionBtn);

        loanBtn = new JButton("Loan Management");
        loanBtn.addActionListener(e -> new LoanManagementUI(currentUser).setVisible(true));
        panel.add(loanBtn);

        reportBtn = new JButton("Reports & Audit");
        reportBtn.addActionListener(e -> new ReportUI(currentUser).setVisible(true));
        panel.add(reportBtn);

        logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginUI().setVisible(true);
        });
        panel.add(logoutBtn);

        // Role-based access
        if ("CUSTOMER".equals(currentUser.getRole())) {
            customerBtn.setEnabled(false);
            accountBtn.setEnabled(false);
            loanBtn.setEnabled(false);
            reportBtn.setEnabled(false);
        }

        add(panel);
    }
}