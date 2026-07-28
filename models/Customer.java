package models;

import java.sql.Timestamp;

public class Customer {
    private int customerId;
    private String name;
    private String cnic;
    private String phone;
    private String address;
    private Timestamp createdAt;

    public Customer() {}

    public Customer(int customerId, String name, String cnic, String phone, String address, Timestamp createdAt) {
        this.customerId = customerId;
        this.name = name;
        this.cnic = cnic;
        this.phone = phone;
        this.address = address;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCnic() { return cnic; }
    public void setCnic(String cnic) { this.cnic = cnic; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}