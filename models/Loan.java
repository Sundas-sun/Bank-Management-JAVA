package models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Loan {
    private int loanId;
    private int customerId;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private String status;
    private Timestamp createdAt;

    public Loan() {}

    public Loan(int loanId, int customerId, BigDecimal amount, BigDecimal interestRate, String status, Timestamp createdAt) {
        this.loanId = loanId;
        this.customerId = customerId;
        this.amount = amount;
        this.interestRate = interestRate;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}