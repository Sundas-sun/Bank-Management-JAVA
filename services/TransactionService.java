package services;

import dao.AccountDAO;
import dao.AuditLogDAO;
import dao.TransactionDAO;
import models.Account;
import models.AuditLog;
import models.Transaction;
import models.User;
import utils.DBConnection;
import utils.Validators;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionService {
    private TransactionDAO transactionDAO = new TransactionDAO();
    private AccountDAO accountDAO = new AccountDAO();
    private AuditLogDAO auditLogDAO = new AuditLogDAO();

    public void deposit(int accountId, BigDecimal amount, User user) throws SQLException, IllegalArgumentException {
        if (!Validators.isValidAmount(amount)) {
            throw new IllegalArgumentException("Invalid amount");
        }
        Account account = accountDAO.getAccountById(accountId);
        if (account == null || !"ACTIVE".equals(account.getStatus())) {
            throw new IllegalArgumentException("Account not found or not active");
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                BigDecimal newBalance = account.getBalance().add(amount);
                accountDAO.updateBalance(conn, accountId, newBalance);

                Transaction transaction = new Transaction();
                transaction.setAccountId(accountId);
                transaction.setType("DEPOSIT");
                transaction.setAmount(amount);
                transactionDAO.createTransaction(conn, transaction);

                AuditLog log = new AuditLog();
                log.setUserId(user.getUserId());
                log.setAction("Deposited " + amount + " to account " + accountId);
                auditLogDAO.logAction(conn, log);

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void withdraw(int accountId, BigDecimal amount, User user) throws SQLException, IllegalArgumentException {
        if (!Validators.isValidAmount(amount)) {
            throw new IllegalArgumentException("Invalid amount");
        }
        Account account = accountDAO.getAccountById(accountId);
        if (account == null || !"ACTIVE".equals(account.getStatus())) {
            throw new IllegalArgumentException("Account not found or not active");
        }
        if (!Validators.canWithdraw(account.getBalance(), amount, account.getAccountType())) {
            throw new IllegalArgumentException("Insufficient balance or minimum balance violation");
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                BigDecimal newBalance = account.getBalance().subtract(amount);
                accountDAO.updateBalance(conn, accountId, newBalance);

                Transaction transaction = new Transaction();
                transaction.setAccountId(accountId);
                transaction.setType("WITHDRAW");
                transaction.setAmount(amount);
                transactionDAO.createTransaction(conn, transaction);

                AuditLog log = new AuditLog();
                log.setUserId(user.getUserId());
                log.setAction("Withdrew " + amount + " from account " + accountId);
                auditLogDAO.logAction(conn, log);

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void transfer(int fromAccountId, int toAccountId, BigDecimal amount, User user) throws SQLException, IllegalArgumentException {
        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("Cannot transfer to same account");
        }
        if (!Validators.isValidAmount(amount)) {
            throw new IllegalArgumentException("Invalid amount");
        }

        Account fromAccount = accountDAO.getAccountById(fromAccountId);
        Account toAccount = accountDAO.getAccountById(toAccountId);
        if (fromAccount == null || !"ACTIVE".equals(fromAccount.getStatus())) {
            throw new IllegalArgumentException("Sender account not found or not active");
        }
        if (toAccount == null || !"ACTIVE".equals(toAccount.getStatus())) {
            throw new IllegalArgumentException("Receiver account not found or not active");
        }
        if (!Validators.canWithdraw(fromAccount.getBalance(), amount, fromAccount.getAccountType())) {
            throw new IllegalArgumentException("Insufficient balance in sender account");
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                BigDecimal newFromBalance = fromAccount.getBalance().subtract(amount);
                accountDAO.updateBalance(conn, fromAccountId, newFromBalance);

                BigDecimal newToBalance = toAccount.getBalance().add(amount);
                accountDAO.updateBalance(conn, toAccountId, newToBalance);

                Transaction debitTx = new Transaction();
                debitTx.setAccountId(fromAccountId);
                debitTx.setType("TRANSFER");
                debitTx.setAmount(amount);
                debitTx.setReferenceAccount(toAccountId);
                transactionDAO.createTransaction(conn, debitTx);

                Transaction creditTx = new Transaction();
                creditTx.setAccountId(toAccountId);
                creditTx.setType("TRANSFER");
                creditTx.setAmount(amount);
                creditTx.setReferenceAccount(fromAccountId);
                transactionDAO.createTransaction(conn, creditTx);

                AuditLog log = new AuditLog();
                log.setUserId(user.getUserId());
                log.setAction("Transferred " + amount + " from account " + fromAccountId + " to " + toAccountId);
                auditLogDAO.logAction(conn, log);

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new SQLException("Transfer failed, rolled back", e);
            }
        }
    }
}