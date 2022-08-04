package com.example.mscustomer.service;

import com.example.mscustomer.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
  public Flux<Customer> findAll();

  public Mono<Customer> findById(String id);

  public Mono<Customer> create(Customer customer);

  public Mono<Customer> update(Customer customer);

  public Mono<Void> delete(String id);
}
