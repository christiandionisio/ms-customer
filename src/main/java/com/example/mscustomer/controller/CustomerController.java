package com.example.mscustomer.controller;

import com.example.mscustomer.dto.CustomerDTO;
import com.example.mscustomer.error.InvalidCustomerTypeException;
import com.example.mscustomer.model.Customer;
import com.example.mscustomer.service.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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

    private ModelMapper modelMapper = new ModelMapper();

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
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Customer> read(@PathVariable String id){
        return customerService.findById(id);
    }

    @PostMapping
    public Mono<ResponseEntity<Customer>> create(@RequestBody CustomerDTO customerDTO){
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return customerService.create(modelMapper.map(customerDTO, Customer.class))
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

    @PutMapping
    public Mono<Customer> update(@RequestBody CustomerDTO customerDTO){
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return customerService.update(modelMapper.map(customerDTO, Customer.class));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id){
        return customerService.delete(id);
    }
}
