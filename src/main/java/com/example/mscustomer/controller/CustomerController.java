package com.example.mscustomer.controller;

import com.example.mscustomer.model.Customer;
import com.example.mscustomer.service.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Value("${owner.name}")
    String name;
    @Value("${server.port}")
    String port;
    private static final Logger logger = LogManager.getLogger(CustomerController.class);

    @GetMapping("/status/check")
    public String status() {
        return "Working on port " + port + " with owner_name " + name;
    }

    @GetMapping
    public Flux<Customer> findAll(){
        logger.debug("Debugging log");
        logger.info("Info log");
        logger.warn("Hey, This is a warning!");
        logger.error("Oops! We have an Error. OK");
        logger.fatal("Damn! Fatal error. Please fix me.");
        Flux<Customer> customerList = customerService.findAll();
        return customerList;
    }

    @GetMapping("/{id}")
    public Mono<Customer> read(@PathVariable String id){
        Mono<Customer> customer = customerService.findById(id);
        return customer;
    }

    @PostMapping
    public Mono<Customer> create(@RequestBody Customer customer){
        Mono<Customer> newCustomer = customerService.create(customer);
        return newCustomer;
    }

    @PutMapping
    public Mono<Customer> update(@RequestBody Customer customer){
        Mono<Customer> customerUpdated = customerService.update(customer);
        return customerUpdated;
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id){
        Mono<Void> customer = customerService.delete(id);
        return customer;
    }
}
