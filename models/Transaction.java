package models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {
    private int transactionId;
    private int accountId;
    private String type;
    private BigDecimal amount;
    private Integer referenceAccount; // Nullable for transfers
    private Timestamp date;

    public Transaction() {}

    public Transaction(int transactionId, int accountId, String type, BigDecimal amount, Integer referenceAccount, Timestamp date) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.referenceAccount = referenceAccount;
        this.date = date;
    }

    // Getters and Setters
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Integer getReferenceAccount() { return referenceAccount; }
    public void setReferenceAccount(Integer referenceAccount) { this.referenceAccount = referenceAccount; }

    public Timestamp getDate() { return date; }
    public void setDate(Timestamp date) { this.date = date; }
}