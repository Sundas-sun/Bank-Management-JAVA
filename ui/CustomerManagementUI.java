package ui;

import models.Customer;
import models.User;
import services.CustomerService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;ad
import java.sql.SQLException;
import java.util.List;

public class CustomerManagementUI extends JFrame {
    private User currentUser;
    private CustomerService customerService;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JButton addBtn, editBtn, deleteBtn, refreshBtn;

    public CustomerManagementUI(User user) {
        this.currentUser = user;
        this.customerService = new CustomerService();
        initializeUI();
        loadCustomers();
    }

    private void initializeUI() {
        setTitle("Customer Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "CNIC", "Phone", "Address"}, 0);
        customerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        addBtn = new JButton("Add Customer");
        addBtn.addActionListener(new AddCustomerAction());
        buttonPanel.add(addBtn);

        editBtn = new JButton("Edit Customer");
        editBtn.addActionListener(new EditCustomerAction());
        buttonPanel.add(editBtn);

        deleteBtn = new JButton("Delete Customer");
        deleteBtn.addActionListener(new DeleteCustomerAction());
        buttonPanel.add(deleteBtn);

        refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadCustomers());
        buttonPanel.add(refreshBtn);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            tableModel.setRowCount(0);
            for (Customer c : customers) {
                tableModel.addRow(new Object[]{c.getCustomerId(), c.getName(), c.getCnic(), c.getPhone(), c.getAddress()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + e.getMessage());
        }
    }

    private class AddCustomerAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField nameField = new JTextField();
            JTextField cnicField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField addressField = new JTextField();

            Object[] message = {
                "Name:", nameField,
                "CNIC:", cnicField,
                "Phone:", phoneField,
                "Phone format: 0300-1234567 or +923001234567",
                "Address:", addressField
            };

            int option = JOptionPane.showConfirmDialog(CustomerManagementUI.this, message, "Add Customer", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    customerService.createCustomer(nameField.getText(), cnicField.getText(), phoneField.getText(), addressField.getText());
                    loadCustomers();
                    JOptionPane.showMessageDialog(CustomerManagementUI.this, "Customer added successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(CustomerManagementUI.this, "Error: " + ex.getMessage());
                }
            }
        }
    }

    private class EditCustomerAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(CustomerManagementUI.this, "Please select a customer to edit.");
                return;
            }
            int customerId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                Customer customer = customerService.getCustomerById(customerId);
                if (customer != null) {
                    JTextField nameField = new JTextField(customer.getName());
                    JTextField phoneField = new JTextField(customer.getPhone());
                    JTextField addressField = new JTextField(customer.getAddress());

                    Object[] message = {
                        "Name:", nameField,
                        "Phone:", phoneField,
                        "Phone format: 0300-1234567 or +923001234567",
                        "Address:", addressField
                    };

                    int option = JOptionPane.showConfirmDialog(CustomerManagementUI.this, message, "Edit Customer", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        customerService.updateCustomer(customerId, nameField.getText(), phoneField.getText(), addressField.getText());
                        loadCustomers();
                        JOptionPane.showMessageDialog(CustomerManagementUI.this, "Customer updated successfully!");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(CustomerManagementUI.this, "Error: " + ex.getMessage());
            }
        }
    }

    private class DeleteCustomerAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(CustomerManagementUI.this, "Please select a customer to delete.");
                return;
            }
            int customerId = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(
                CustomerManagementUI.this,
                "Are you sure you want to permanently delete this customer?",
                "Delete Customer",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            try {
                customerService.deleteCustomer(customerId);
                loadCustomers();
                JOptionPane.showMessageDialog(CustomerManagementUI.this, "Customer deleted successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(CustomerManagementUI.this, "Error: " + ex.getMessage());
            }
        }
    }
}