package com.example.mscustomer.provider;

import com.example.mscustomer.dto.CustomerDto;
import com.example.mscustomer.enums.CustomerTypeEnum;
import com.example.mscustomer.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerProvider {

  public static List<Customer> getCustomerList(){
    List<Customer> customerList = new ArrayList<>();
    customerList.add(getCustomer());
    return customerList;
  }

  public static Customer getCustomer() {
    Customer customer = new Customer();
    customer.setCustomerId("1");
    customer.setName("Sofia");
    customer.setLastName("Jimenez");
    customer.setEmail("sofia@gmail.com");
    customer.setDocumentType("DNI");
    customer.setDocumentNumber("76234574");
    customer.setCustomerType(CustomerTypeEnum.PERSONNEL.getValue());
    customer.setCategory("VIP");
    return customer;
  }

  public static CustomerDto getCustomerDto() {
    CustomerDto customer = new CustomerDto();
    customer.setCustomerId("1");
    customer.setName("Sofia");
    customer.setLastName("Jimenez");
    customer.setEmail("sofia@gmail.com");
    customer.setDocumentType("DNI");
    customer.setDocumentNumber("76234574");
    customer.setCustomerType(CustomerTypeEnum.PERSONNEL.getValue());
    customer.setCategory("VIP");
    return customer;
  }
}
