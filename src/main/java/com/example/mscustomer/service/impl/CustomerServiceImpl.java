package com.example.mscustomer.service.impl;

import com.example.mscustomer.model.daos.CustomerDao;
import com.example.mscustomer.model.documents.Customer;
import com.example.mscustomer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public Flux<Customer> findAll() {
        return customerDao.findAll();
    }

    @Override
    public Mono<Customer> findById(String id) {
        return customerDao.findById(id);
    }

    @Override
    public Mono<Customer> save(Customer curso) {
        return customerDao.save(curso);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return customerDao.deleteById(id);
    }
}
