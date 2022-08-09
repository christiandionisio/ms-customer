package com.example.mscustomer.controller;

import com.example.mscustomer.dto.CustomerDto;
import com.example.mscustomer.error.InvalidCustomerTypeException;
import com.example.mscustomer.model.Customer;
import com.example.mscustomer.provider.CustomerProvider;
import com.example.mscustomer.service.CustomerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {

  @MockBean
  CustomerService customerService;

  @Autowired
  private WebTestClient webClient;

  @BeforeEach
  void setUp() {
  }

  @Test
  @DisplayName("Get all customers")
  void findAll() {
    Mockito.when(customerService.findAll())
            .thenReturn(Flux.fromIterable(CustomerProvider.getCustomerList()));

    webClient.get()
            .uri("/customers")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBodyList(Customer.class)
            .consumeWith(response -> {
              List<Customer> customerList = response.getResponseBody();
              customerList.forEach(c -> {
                System.out.println(c.getName());
              });
              Assertions.assertThat(customerList.size() > 0).isTrue();
            });
    //.hasSize(1);
  }

  @Test
  @DisplayName("Create customer")
  void create() {
    Mockito.when(customerService.create(Mockito.any(Customer.class)))
            .thenReturn(Mono.just(CustomerProvider.getCustomer()));

    webClient.post().uri("/customers")
            .body(Mono.just(CustomerProvider.getCustomerDto()), CustomerDto.class)
            .exchange()
            .expectStatus().isCreated();
  }

  @Test
  @DisplayName("Create customer with InvalidCustomerTypeException")
  void createWithInvalidCustomerTypeException() {
    Mockito.when(customerService.create(Mockito.any(Customer.class)))
            .thenReturn(Mono.error(new InvalidCustomerTypeException()));

    webClient.post().uri("/customers")
            .body(Mono.just(CustomerProvider.getCustomerDto()), CustomerDto.class)
            .exchange()
            .expectStatus().isBadRequest();
  }

  @Test
  @DisplayName("Create customer with GeneralException")
  void createWithGeneralException() {
    Mockito.when(customerService.create(Mockito.any(Customer.class)))
            .thenReturn(Mono.error(new Exception("GeneralException TEST")));

    webClient.post().uri("/customers")
            .body(Mono.just(CustomerProvider.getCustomerDto()), CustomerDto.class)
            .exchange()
            .expectStatus().is5xxServerError();
  }

  @Test
  @DisplayName("Read customer")
  void read() {
    Mockito.when(customerService.findById(Mockito.anyString()))
            .thenReturn(Mono.just(CustomerProvider.getCustomer()));

    webClient.get().uri("/customers/1")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBody()
            .jsonPath("$.customerId").isNotEmpty()
            .jsonPath("$.name").isEqualTo(CustomerProvider.getCustomer().getName());
  }

  @Test
  @DisplayName("Update customer")
  void update() {
    Mockito.when(customerService.findById(Mockito.anyString()))
            .thenReturn(Mono.just(CustomerProvider.getCustomer()));
    Mockito.when(customerService.update(Mockito.any(Customer.class)))
            .thenReturn(Mono.just(CustomerProvider.getCustomer()));

    webClient.put().uri("/customers/1")
            .body(Mono.just(CustomerProvider.getCustomerDto()), CustomerDto.class)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Customer.class)
            .isEqualTo(CustomerProvider.getCustomer());
  }

  @Test
  @DisplayName("Delete customer")
  void delete() {
    Mockito.when(customerService.findById(Mockito.anyString()))
            .thenReturn(Mono.just(CustomerProvider.getCustomer()));
    Mockito.when(customerService.delete(Mockito.anyString()))
            .thenReturn(Mono.empty());

    webClient.delete().uri("/customers/1")
            .exchange()
            .expectStatus().isNoContent();
  }
}
