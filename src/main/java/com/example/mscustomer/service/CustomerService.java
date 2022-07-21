package com.example.mscustomer.service;


import com.example.mscustomer.model.documents.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    public Flux<Customer> findAll();
    public Mono<Customer> findById(String id);
    public Mono<Customer> save(Customer curso);
    public Mono<Void> deleteById(String id);
}
