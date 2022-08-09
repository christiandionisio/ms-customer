package com.example.mscustomer.dto;

import java.util.Date;
import lombok.Data;


@Data
public class CustomerDto {
  private String customerId;
  private String name;
  private String lastName;
  private String email;
  private String documentType;
  private String documentNumber;
  private Date birthDate;
  private String customerType;
  private String category;
}
