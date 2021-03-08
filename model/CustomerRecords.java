/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;

/**
 *
 * @author laron
 */
public class CustomerRecords {
    
    private static ObservableList<CustomerRecords> allCustomers = 
            FXCollections.observableArrayList();
    private static ObservableList<CustomerRecords> customerSearch = 
            FXCollections.observableArrayList();    
    
    int customerId;
    String customerName;   
    String address;
    String address2;
    String postalCode;    
    String city;
    String country;
    String phone;
    
    //constructor
    public CustomerRecords(int cmId, String cmName, String cmAddress, 
            String cmAddress2, String zipCode, String city, String country, String phone) {
        
        this.customerId = cmId;
        this.customerName = cmName;
        this.address = cmAddress;
        this.address2 = cmAddress2;
        this.postalCode = zipCode;
        this.city = city;
        this.country = country;
        this.phone = phone;
        
    }
    
    
    //getters and setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    //methods
    public static ObservableList<CustomerRecords> getAllCustomers() {
        
        return allCustomers;
    }
    
    public static void searchRecords(int cmId){
        
        //work in progress
    }
    
    public static void addRecord(CustomerRecords newCustomer){
        
        allCustomers.add(newCustomer);
    }
    
    public static boolean inputException(String customerName, String address, 
                            String address2, String city, String country, 
                            String postalCode, String phone) {
        boolean flag = false;       
        String[] inputs = new String[] {customerName, address, city, 
            country, postalCode, phone};
        for(String val : inputs) {
            
            if(val.equals("")) {
                
                flag = true;
            }
        }    
        return flag;
    }
}
