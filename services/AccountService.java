package services;

import dao.AccountDAO;
import models.Account;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class AccountService {
    private AccountDAO accountDAO = new AccountDAO();

    public void createAccount(int customerId, String type, BigDecimal initialBalance) throws SQLException {
        Account account = new Account();
        account.setCustomerId(customerId);
        account.setAccountType(type);
        account.setBalance(initialBalance);
        account.setStatus("ACTIVE");
        accountDAO.createAccount(account);
    }

    public Account getAccountById(int id) throws SQLException {
        return accountDAO.getAccountById(id);
    }

    public List<Account> getAccountsByCustomer(int customerId) throws SQLException {
        return accountDAO.getAccountsByCustomerId(customerId);
    }

    public List<Account> getAllAccounts() throws SQLException {
        return accountDAO.getAllAccounts();
    }

    public void freezeAccount(int accountId) throws SQLException {
        accountDAO.updateStatus(accountId, "FROZEN");
    }

    public void closeAccount(int accountId) throws SQLException {
        accountDAO.updateStatus(accountId, "CLOSED");
    }

    public void deleteAccount(int accountId) throws SQLException {
        accountDAO.deleteAccount(accountId);
    }
}