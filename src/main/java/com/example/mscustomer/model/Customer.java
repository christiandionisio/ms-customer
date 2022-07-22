package com.example.mscustomer.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection="customers")
public class Customer {
    @Id
    private String customerId;
    private String name;
    private String lastName;
    private String email ;
    private String documentType;
    private String documentNumber;
    private Date birthDate;
    private String customerType;
}
