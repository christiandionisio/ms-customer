package com.example.mscustomer.ws;

import com.example.mscustomer.model.documents.Customer;
import com.example.mscustomer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class CustomerRestController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public Flux<Customer> getCustomers(){
        Flux<Customer> customerList = customerService.findAll();
        return customerList;
    }

    @GetMapping("/customers/{id}")
    public Mono<Customer> getCustomer(@PathVariable String id){
        Mono<Customer> customer = customerService.findById(id);
        return customer;
    }

    @PostMapping("/customers")
    public Mono<Customer> saveCustomer(@RequestBody Customer customer){
        Mono<Customer> newCustomer = customerService.save(customer);
        return newCustomer;
    }

    @PutMapping("/customers")
    public Mono<Customer> updateCustomer(@RequestBody Customer customer){
        Mono<Customer> customerUpdated = customerService.save(customer);
        return customerUpdated;
    }

    @DeleteMapping("/customers/{id}")
    public Mono<Void> deleteCustomer(@PathVariable String id){
        Mono<Void> customer = customerService.deleteById(id);
        return customer;
    }
}
