package com.example.mscustomer.controller;

import com.example.mscustomer.error.InvalidCustomerTypeException;
import com.example.mscustomer.model.Customer;
import com.example.mscustomer.service.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

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
    public Mono<ResponseEntity<Flux<Customer>>> findAll(){
        logger.debug("Debugging log");
        logger.info("Info log");
        logger.warn("Hey, This is a warning!");
        logger.error("Oops! We have an Error. OK");
        logger.fatal("Damn! Fatal error. Please fix me.");
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(customerService.findAll()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Customer>> read(@PathVariable String id){
        return customerService.findById(id).map(customer -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(customer))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Customer>> create(@RequestBody Customer customer){
        return customerService.create(customer)
                .flatMap(c -> Mono.just(ResponseEntity.created(URI.create("http://localhost:8082/customers".concat("/").concat(c.getCustomerId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c)))
                .onErrorResume(e -> {
                    if (e instanceof InvalidCustomerTypeException) {
                        return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
                    }
                    return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Customer>> update(@RequestBody Customer customer, @PathVariable String id){
        return customerService.findById(id).flatMap(c -> {
                    c.setName(customer.getName());
                    c.setLastName(customer.getLastName());
                    c.setEmail(customer.getEmail());
                    c.setDocumentType(customer.getDocumentType());
                    c.setDocumentNumber(customer.getDocumentNumber());
                    c.setCustomerType(customer.getCustomerType());
                    c.setCategory(customer.getCategory());
                    return customerService.update(c);
                }).map(customerUpdated -> ResponseEntity.created(URI.create("/api/productos/".concat(customerUpdated.getCustomerId())))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(customerUpdated))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id){
        return customerService.findById(id).flatMap(customer -> {
            return customerService.delete(customer.getCustomerId()).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }
}
