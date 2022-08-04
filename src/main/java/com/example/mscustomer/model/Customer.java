package com.example.mscustomer.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
  @Id
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
