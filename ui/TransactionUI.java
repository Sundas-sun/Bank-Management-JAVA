package ui;

import models.User;
import services.TransactionService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;

public class TransactionUI extends JFrame {
    private User currentUser;
    private TransactionService transactionService;
    private JTextField accountIdField, amountField, toAccountField;
    private JComboBox<String> typeCombo;
    private JButton executeBtn;

    public TransactionUI(User user) {
        this.currentUser = user;
        this.transactionService = new TransactionService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Transaction Management");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Transaction Type:"));
        typeCombo = new JComboBox<>(new String[]{"DEPOSIT", "WITHDRAW", "TRANSFER"});
        panel.add(typeCombo);

        panel.add(new JLabel("Account ID:"));
        accountIdField = new JTextField();
        panel.add(accountIdField);

        panel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        panel.add(amountField);

        panel.add(new JLabel("To Account (for transfer):"));
        toAccountField = new JTextField();
        panel.add(toAccountField);

        executeBtn = new JButton("Execute");
        executeBtn.addActionListener(new ExecuteAction());
        panel.add(executeBtn);

        add(panel);
    }

    private class ExecuteAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String type = (String) typeCombo.getSelectedItem();
                int accountId = Integer.parseInt(accountIdField.getText());
                BigDecimal amount = new BigDecimal(amountField.getText());

                if ("DEPOSIT".equals(type)) {
                    transactionService.deposit(accountId, amount, currentUser);
                    JOptionPane.showMessageDialog(TransactionUI.this, "Deposit successful!");
                } else if ("WITHDRAW".equals(type)) {
                    transactionService.withdraw(accountId, amount, currentUser);
                    JOptionPane.showMessageDialog(TransactionUI.this, "Withdrawal successful!");
                } else if ("TRANSFER".equals(type)) {
                    int toAccountId = Integer.parseInt(toAccountField.getText());
                    transactionService.transfer(accountId, toAccountId, amount, currentUser);
                    JOptionPane.showMessageDialog(TransactionUI.this, "Transfer successful!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(TransactionUI.this, "Invalid number format");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(TransactionUI.this, "Database error: " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(TransactionUI.this, ex.getMessage());
            }
        }
    }
}