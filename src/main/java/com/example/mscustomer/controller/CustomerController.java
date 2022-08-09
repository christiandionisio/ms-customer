package com.example.mscustomer.controller;

import com.example.mscustomer.dto.CustomerDto;
import com.example.mscustomer.error.InvalidCustomerTypeException;
import com.example.mscustomer.model.Customer;
import com.example.mscustomer.service.CustomerService;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller layer of Customer Product.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
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

  /**
   * Get list of Customers.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @GetMapping
  public Mono<ResponseEntity<Flux<Customer>>> findAll() {
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

  /**
   * Get detail of a customer by Id.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @GetMapping("/{id}")
  public Mono<ResponseEntity<Customer>> read(@PathVariable String id) {
    return customerService.findById(id).map(customer -> ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(customer))
            .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**
   * Create a Customer.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @PostMapping
  public Mono<ResponseEntity<Customer>> create(@RequestBody CustomerDto customerDto) {
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    return customerService.create(modelMapper.map(customerDto, Customer.class))
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

  /**
   * Update Customer By Id.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @PutMapping("/{id}")
  public Mono<ResponseEntity<Customer>> update(@RequestBody CustomerDto customerDto,
                                               @PathVariable String id) {
    return customerService.findById(id).flatMap(c -> {
      c.setName(customerDto.getName());
      c.setLastName(customerDto.getLastName());
      c.setEmail(customerDto.getEmail());
      c.setDocumentType(customerDto.getDocumentType());
      c.setDocumentNumber(customerDto.getDocumentNumber());
      c.setCustomerType(customerDto.getCustomerType());
      c.setCategory(customerDto.getCategory());
      return customerService.update(c);
    }).map(customerUpdated -> ResponseEntity
                    .created(URI.create("/api/productos/".concat(customerUpdated.getCustomerId())))
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(customerUpdated))
            .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**
   * Delete Customer By Id.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
    return customerService.findById(id).flatMap(customer -> {
      return customerService.delete(customer.getCustomerId())
              .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
    }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
  }
}
