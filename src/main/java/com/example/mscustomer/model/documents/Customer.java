package com.example.mscustomer.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection="customers")
public class Customer {
    @Id
    private String customer_id;

    private String name;
    private String last_name;
    private String email ;
    private String document_type;
    private String document_number;
    private Date birth_date;
}
