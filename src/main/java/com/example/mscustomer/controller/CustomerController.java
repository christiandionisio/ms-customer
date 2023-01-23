package com.example.mscustomer.controller;

import com.example.mscustomer.dto.CustomerDto;
import com.example.mscustomer.error.InvalidCustomerTypeException;
import com.example.mscustomer.model.Customer;
import com.example.mscustomer.service.CustomerService;
import java.net.URI;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(value = "Customer Rest Controller", description = "REST API for Customers")
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
  @ApiOperation(value = "Get Customer ", response = Customer.class, tags = "getCustomers")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Success|OK"),
          @ApiResponse(code = 401, message = "Not Authorized!"),
          @ApiResponse(code = 403, message = "Forbidden!"),
          @ApiResponse(code = 404, message = "Not Found!") })
  @GetMapping
  public Mono<ResponseEntity<Flux<Customer>>> findAll() {
    return Mono.just(
            ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(customerService.findAll()));
  }

  /**
   * Create a Customer.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @ApiOperation(value = "Create Customer ", response = Customer.class, tags = "createCustomer")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Success|OK"),
          @ApiResponse(code = 401, message = "Not Authorized!"),
          @ApiResponse(code = 403, message = "Forbidden!"),
          @ApiResponse(code = 404, message = "Not Found!") })
  @PostMapping
  public Mono<ResponseEntity<Customer>> create(@RequestBody CustomerDto customerDto) {
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    return customerService.create(modelMapper.map(customerDto, Customer.class))
            .flatMap(c -> Mono.just(ResponseEntity.created(URI.create("http://localhost:8082/customers".concat("/").concat(c.getCustomerId())))
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(c)))
            .onErrorResume(e -> {
              if (e instanceof InvalidCustomerTypeException) {
                logger.error(e.getMessage());
                return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
              }
              logger.error(e.getMessage());
              return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
            })
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  /**
   * Get detail of a customer by Id.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @ApiOperation(value = "Get Customer by Id ", response = Customer.class, tags = "getCustomerById")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Success|OK"),
          @ApiResponse(code = 401, message = "Not Authorized!"),
          @ApiResponse(code = 403, message = "Forbidden!"),
          @ApiResponse(code = 404, message = "Not Found!") })
  @GetMapping("/{id}")
  public Mono<ResponseEntity<Customer>> read(@PathVariable String id) {
    return customerService.findById(id).map(customer -> ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(customer))
            .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**
   * Update Customer By Id.
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @ApiOperation(value = "Update Customer ", response = Customer.class, tags = "updateCustomer")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Success|OK"),
          @ApiResponse(code = 401, message = "Not Authorized!"),
          @ApiResponse(code = 403, message = "Forbidden!"),
          @ApiResponse(code = 404, message = "Not Found!") })
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
                    .created(URI.create("/customers/".concat(customerUpdated.getCustomerId())))
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
  @ApiOperation(value = "Delete Customer ", response = Customer.class, tags = "deleteCustomer")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Success|OK"),
          @ApiResponse(code = 401, message = "Not Authorized!"),
          @ApiResponse(code = 403, message = "Forbidden!"),
          @ApiResponse(code = 404, message = "Not Found!") })
  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
    return customerService.findById(id)
            .flatMap(customer -> customerService.delete(customer.getCustomerId())
              .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
            .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
  }

  /**
   * Get summary of available products by customer ID
   *
   * @author Alisson Arteaga / Christian Dionisio
   * @version 1.0
   */
  @GetMapping("/getSummaryOfAvailableProductsByCustomer/{id}")
    public Mono<ResponseEntity<Object>> getSummaryOfAvailableProductsByCustomer(@PathVariable String id) {
        return customerService.getSummaryOfAvailableProductsByCustomer(id)
                .flatMap(c -> Mono.just(ResponseEntity.ok().body(c)));
    }
}
