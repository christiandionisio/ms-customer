package com.example.mscustomer.service;

import com.example.mscustomer.enums.CustomerTypeEnum;
import com.example.mscustomer.error.InvalidCustomerTypeException;
import com.example.mscustomer.repository.CustomerRepository;
import com.example.mscustomer.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerDao;

    @Override
    public Flux<Customer> findAll() {
        return customerDao.findAll();
    }

    @Override
    public Mono<Customer> findById(String id) {
        return customerDao.findById(id);
    }

    @Override
    public Mono<Customer> create(Customer customer) {
        if(customer.getCustomerType().equals(CustomerTypeEnum.BUSINESS.getValue()) || customer.getCustomerType().equals(CustomerTypeEnum.PERSONNEL.getValue())){
            return customerDao.save(customer);
        }else{
            return Mono.error(new InvalidCustomerTypeException());
        }
    }

    @Override
    public Mono<Customer> update(Customer customer) {
        return customerDao.save(customer);
    }

    @Override
    public Mono<Void> delete(String id) {
        return customerDao.deleteById(id);
    }
}
