package com.example.mscustomer.model.daos;

import com.example.mscustomer.model.documents.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDao extends ReactiveMongoRepository<Customer, String> {
}
