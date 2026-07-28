package dao;

import models.Account;
import utils.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public void createAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (customer_id, account_type, balance, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, account.getCustomerId());
            stmt.setString(2, account.getAccountType());
            stmt.setBigDecimal(3, account.getBalance());
            stmt.setString(4, account.getStatus());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                account.setAccountId(rs.getInt(1));
            }
        }
    }

    public Account getAccountById(int accountId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getInt("customer_id"),
                    rs.getString("account_type"),
                    rs.getBigDecimal("balance"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
                );
            }
        }
        return null;
    }

    public List<Account> getAccountsByCustomerId(int customerId) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                accounts.add(new Account(
                    rs.getInt("account_id"),
                    rs.getInt("customer_id"),
                    rs.getString("account_type"),
                    rs.getBigDecimal("balance"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
                ));
            }
        }
        return accounts;
    }

    public void updateBalance(int accountId, BigDecimal newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, newBalance);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
        }
    }

    public void updateBalance(Connection conn, int accountId, BigDecimal newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, newBalance);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
        }
    }

    public void updateStatus(int accountId, String status) throws SQLException {
        String sql = "UPDATE accounts SET status = ? WHERE account_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
        }
    }

    public void deleteAccount(int accountId) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            stmt.executeUpdate();
        }
    }

    public List<Account> getAllAccounts() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                accounts.add(new Account(
                    rs.getInt("account_id"),
                    rs.getInt("customer_id"),
                    rs.getString("account_type"),
                    rs.getBigDecimal("balance"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
                ));
            }
        }
        return accounts;
    }
}