package com.example.mscustomer.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerDTO {
    private String customerId;
    private String name;
    private String lastName;
    private String email ;
    private String documentType;
    private String documentNumber;
    private Date birthDate;
    private String customerType;
    private String category;
}
