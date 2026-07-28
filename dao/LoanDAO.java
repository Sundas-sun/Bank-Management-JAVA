package dao;

import models.Loan;
import utils.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {
    public void createLoan(Loan loan) throws SQLException {
        String sql = "INSERT INTO loans (customer_id, amount, interest_rate, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, loan.getCustomerId());
            stmt.setBigDecimal(2, loan.getAmount());
            stmt.setBigDecimal(3, loan.getInterestRate());
            stmt.setString(4, loan.getStatus());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                loan.setLoanId(rs.getInt(1));
            }
        }
    }

    public List<Loan> getLoansByCustomerId(int customerId) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE customer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                loans.add(new Loan(
                    rs.getInt("loan_id"),
                    rs.getInt("customer_id"),
                    rs.getBigDecimal("amount"),
                    rs.getBigDecimal("interest_rate"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
                ));
            }
        }
        return loans;
    }

    public void updateLoanStatus(int loanId, String status) throws SQLException {
        String sql = "UPDATE loans SET status = ? WHERE loan_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, loanId);
            stmt.executeUpdate();
        }
    }

    public void deleteLoan(int loanId) throws SQLException {
        String sql = "DELETE FROM loans WHERE loan_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, loanId);
            stmt.executeUpdate();
        }
    }

    public List<Loan> getAllLoans() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                loans.add(new Loan(
                    rs.getInt("loan_id"),
                    rs.getInt("customer_id"),
                    rs.getBigDecimal("amount"),
                    rs.getBigDecimal("interest_rate"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
                ));
            }
        }
        return loans;
    }
}