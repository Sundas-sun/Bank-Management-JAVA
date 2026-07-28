# Bank-Management-JAVA
#
   - Download from https://dev.mysql.com/downloads/connector/j/
   - Place `mysql-connector-java-x.x.x.jar` in the project root or add to classpath

3. **Create Database**:
   - Open MySQL command line or MySQL Workbench
   - Run the `schema.sql` script to create the database and tables

4. **Configure Database Connection**:
   - Edit `utils/DBConnection.java` to match your MySQL credentials:
     ```java
     private static final String URL = "jdbc:mysql://localhost:3306/bank_system";
     private static final String USER = "your_username";
     private static final String PASSWORD = "your_password";
     ```

5. **Add JDBC Driver**:
   - Download MySQL Connector/J from https://dev.mysql.com/downloads/connector/j/
   - Copy the JAR to `lib\mysql-connector-java.jar`

6. **Compile and Run**:
   - Use the provided helper script:
     ```bat
     run.bat
     ```
   - Or compile manually with the JAR on the classpath:
     ```bat
     javac -cp "lib\mysql-connector-java.jar" *.java models\*.java dao\*.java services\*.java utils\*.java ui\*.java
     java -cp "bin;lib\mysql-connector-java.jar" Main
     ```

## Default Users

- **Admin**: username: admin, password: admin123
- **Teller**: username: teller1, password: teller123
- **Customer**: username: customer1, password: customer123

*Note: Update passwords in schema.sql with hashed versions using PasswordUtils.hashPassword()*

## Project Structure

```
bank-system/
├── Main.java                 # Application entry point
├── schema.sql               # Database schema and sample data
├── models/                  # Data models
│   ├── User.java
│   ├── Customer.java
│   ├── Account.java
│   ├── Transaction.java
│   ├── Loan.java
│   └── AuditLog.java
├── dao/                     # Data Access Objects
│   ├── UserDAO.java
│   ├── CustomerDAO.java
│   ├── AccountDAO.java
│   ├── TransactionDAO.java
│   ├── LoanDAO.java
│   └── AuditLogDAO.java
├── services/                # Business logic
│   ├── AuthenticationService.java
│   ├── CustomerService.java
│   ├── AccountService.java
│   ├── TransactionService.java
│   └── LoanService.java
├── utils/                   # Utilities
│   ├── DBConnection.java
│   ├── PasswordUtils.java
│   └── Validators.java
└── ui/                      # Swing UI forms
    ├── LoginUI.java
    ├── DashboardUI.java
    ├── CustomerManagementUI.java
    ├── AccountManagementUI.java
    ├── TransactionUI.java
    ├── LoanManagementUI.java
    └── ReportUI.java
```

## Key Technical Features

- **ACID Transactions**: Transfer operations use SQL transactions with rollback on failure
- **Role-Based Access**: Different dashboards based on user role
- **Audit Trail**: All actions logged with user and timestamp
- **Validation**: CNIC uniqueness, balance checks, account status validation
- **MVC Architecture**: Separation of concerns with Models, Views (UI), Controllers (Services)

## Sample Data

The schema.sql includes sample users, customers, and accounts for testing.

## Future Enhancements

- PDF statement generation
- Interest calculation for savings
- Card management
- Advanced reporting with filters
- Email notifications Bank Management System

A transaction-safe, role-based financial system with audit tracking built using Java Swing and MySQL.

## Features

- **Authentication & Roles**: Admin, Teller, Customer with secure login
- **Customer Management**: Create and manage customer profiles with KYC
- **Account Management**: Savings and Current accounts with balance tracking
- **Transaction System**: Deposit, Withdraw, Transfer with ACID properties
- **Loan Management**: Apply and approve loans
- **Audit Logging**: Track all actions for compliance

## Prerequisites

- Java JDK 8 or higher
- MySQL Server
- MySQL Connector/J (JDBC driver)

## Setup Instructions

1. **Install MySQL**:
   - Download and install MySQL from https://dev.mysql.com/downloads/mysql/
   - Start MySQL service

2. **Download MySQL Connector/J**:
