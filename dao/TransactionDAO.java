package dao;

import models.Transaction;
import utils.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    public void createTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (account_id, type, amount, reference_account) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, transaction.getAccountId());
            stmt.setString(2, transaction.getType());
            stmt.setBigDecimal(3, transaction.getAmount());
            if (transaction.getReferenceAccount() != null) {
                stmt.setInt(4, transaction.getReferenceAccount());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                transaction.setTransactionId(rs.getInt(1));
            }
        }
    }

    public void createTransaction(Connection conn, Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (account_id, type, amount, reference_account) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, transaction.getAccountId());
            stmt.setString(2, transaction.getType());
            stmt.setBigDecimal(3, transaction.getAmount());
            if (transaction.getReferenceAccount() != null) {
                stmt.setInt(4, transaction.getReferenceAccount());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                transaction.setTransactionId(rs.getInt(1));
            }
        }
    }

    public List<Transaction> getTransactionsByAccountId(int accountId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transactions.add(new Transaction(
                    rs.getInt("transaction_id"),
                    rs.getInt("account_id"),
                    rs.getString("type"),
                    rs.getBigDecimal("amount"),
                    rs.getObject("reference_account", Integer.class),
                    rs.getTimestamp("date")
                ));
            }
        }
        return transactions;
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY date DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                transactions.add(new Transaction(
                    rs.getInt("transaction_id"),
                    rs.getInt("account_id"),
                    rs.getString("type"),
                    rs.getBigDecimal("amount"),
                    rs.getObject("reference_account", Integer.class),
                    rs.getTimestamp("date")
                ));
            }
        }
        return transactions;
    }
}