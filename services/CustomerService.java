package services;

import dao.CustomerDAO;
import models.Customer;
import utils.Validators;

import java.sql.SQLException;
import java.util.List;

public class CustomerService {
    private CustomerDAO customerDAO = new CustomerDAO();

    public void createCustomer(String name, String cnic, String phone, String address) throws SQLException, IllegalArgumentException {
        if (!Validators.isValidCNIC(cnic)) {
            throw new IllegalArgumentException("Invalid CNIC format");
        }
        if (!Validators.isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone format");
        }
        Customer existing = customerDAO.getCustomerByCNIC(cnic);
        if (existing != null) {
            throw new IllegalArgumentException("CNIC already exists");
        }
        Customer customer = new Customer();
        customer.setName(name);
        customer.setCnic(cnic);
        customer.setPhone(phone);
        customer.setAddress(address);
        customerDAO.createCustomer(customer);
    }

    public Customer getCustomerById(int id) throws SQLException {
        return customerDAO.getCustomerById(id);
    }

    public List<Customer> getAllCustomers() throws SQLException {
        return customerDAO.getAllCustomers();
    }

    public void updateCustomer(int id, String name, String phone, String address) throws SQLException {
        Customer customer = customerDAO.getCustomerById(id);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }
        customer.setName(name);
        customer.setPhone(phone);
        customer.setAddress(address);
        customerDAO.updateCustomer(customer);
    }

    public void deleteCustomer(int id) throws SQLException {
        customerDAO.deleteCustomer(id);
    }
}