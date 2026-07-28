package dao;

import models.Customer;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public void createCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (name, cnic, phone, address) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getCnic());
            stmt.setString(3, customer.getPhone());
            stmt.setString(4, customer.getAddress());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                customer.setCustomerId(rs.getInt(1));
            }
        }
    }

    public Customer getCustomerById(int customerId) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("cnic"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getTimestamp("created_at")
                );
            }
        }
        return null;
    }

    public Customer getCustomerByCNIC(String cnic) throws SQLException {
        String sql = "SELECT * FROM customers WHERE cnic = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cnic);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("cnic"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getTimestamp("created_at")
                );
            }
        }
        return null;
    }

    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("cnic"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getTimestamp("created_at")
                ));
            }
        }
        return customers;
    }

    public void updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET name = ?, phone = ?, address = ? WHERE customer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getAddress());
            stmt.setInt(4, customer.getCustomerId());
            stmt.executeUpdate();
        }
    }

    public void deleteCustomer(int customerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
        }
    }
}