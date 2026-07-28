package services;

import dao.LoanDAO;
import models.Loan;
import models.User;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class LoanService {
    private LoanDAO loanDAO = new LoanDAO();

    public void applyForLoan(int customerId, BigDecimal amount, BigDecimal interestRate) throws SQLException {
        Loan loan = new Loan();
        loan.setCustomerId(customerId);
        loan.setAmount(amount);
        loan.setInterestRate(interestRate);
        loan.setStatus("PENDING");
        loanDAO.createLoan(loan);
    }

    public void approveLoan(int loanId, User user) throws SQLException {
        loanDAO.updateLoanStatus(loanId, "APPROVED");
        // Could add audit log here
    }

    public void rejectLoan(int loanId, User user) throws SQLException {
        loanDAO.updateLoanStatus(loanId, "REJECTED");
        // Could add audit log here
    }

    public void deleteLoan(int loanId) throws SQLException {
        loanDAO.deleteLoan(loanId);
    }

    public List<Loan> getLoansByCustomer(int customerId) throws SQLException {
        return loanDAO.getLoansByCustomerId(customerId);
    }

    public List<Loan> getAllLoans() throws SQLException {
        return loanDAO.getAllLoans();
    }
}