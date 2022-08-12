package com.example.mscustomer.service;

import com.example.mscustomer.dto.AvailableProductsDto;
import com.example.mscustomer.dto.ProductAvailableDto;
import com.example.mscustomer.enums.AccountTypeEnum;
import com.example.mscustomer.enums.CustomerTypeEnum;
import com.example.mscustomer.error.InvalidCustomerTypeException;
import com.example.mscustomer.model.Customer;
import com.example.mscustomer.repository.CustomerRepository;
import com.example.mscustomer.util.CustomerBusinessUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

  @Autowired
  private CustomerRepository customerDao;

  @Autowired
  private CustomerBusinessUtil customerBusinessUtil;

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
    if (customer.getCustomerType().equals(CustomerTypeEnum.BUSINESS.getValue()) || customer.getCustomerType().equals(CustomerTypeEnum.PERSONNEL.getValue())) {
      return customerDao.save(customer);
    } else {
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

  @Override
  public Mono<AvailableProductsDto> getSummaryOfAvailableProductsByCustomer(String idCustomer) {
    AvailableProductsDto availableProductsDto = new AvailableProductsDto();
    return customerDao.findById(idCustomer)
            .flatMap(customer -> validateAccountProductsForCustomerType(customer)
                    .flatMap(productAvailableDto -> {
                      availableProductsDto.setAccountProductDtoList(productAvailableDto);
                      return Mono.just(availableProductsDto);
                    })
                    .flatMap(response -> validateCreditProductsForCustomerType(customer)
                            .flatMap(productAvailableDto -> {
                              availableProductsDto.setCreditProductDtoList(productAvailableDto);
                              return Mono.just(availableProductsDto);
                            })
                            .defaultIfEmpty(availableProductsDto)
                    )
            );
  }

  public Mono<List<ProductAvailableDto>> validateAccountProductsForCustomerType(Customer customer) {
    return (customer.getCustomerType().equals(CustomerTypeEnum.BUSINESS.getValue()))
            ? validateAccountProductsForBusinessCustomer(customer)
            : validateAccountProductsForPersonalCustomer(customer);
  }

  private Mono<List<ProductAvailableDto>> validateAccountProductsForBusinessCustomer(Customer customer) {
    List<ProductAvailableDto> productAvailableDtoList = new ArrayList<>();
    productAvailableDtoList.add(ProductAvailableDto.builder()
                    .productName("ACCOUNT")
                    .productDescription("DISPONIBLE")
                    .productCategory(AccountTypeEnum.CURRENT_ACCOUNT.getValue())
                    .build());
    return Mono.just(productAvailableDtoList);
  }

  private Mono<List<ProductAvailableDto>> validateAccountProductsForPersonalCustomer(Customer customer) {
    Map<String, Boolean> accountsAvailable = new HashMap<>();
    return customerBusinessUtil.findByCustomerOwnerId(customer.getCustomerId())
            .map(accountResponse -> {
              if (accountResponse.getAccountType().equals(AccountTypeEnum.SAVING_ACCOUNT.getValue())) {
                accountsAvailable.put(AccountTypeEnum.SAVING_ACCOUNT.getValue(), false);
              } else if (accountResponse.getAccountType().equals(AccountTypeEnum.CURRENT_ACCOUNT.getValue())) {
                accountsAvailable.put(AccountTypeEnum.CURRENT_ACCOUNT.getValue(), false);
              } else if (accountResponse.getAccountType().equals(AccountTypeEnum.DEPOSIT_ACCOUNT.getValue())) {
                accountsAvailable.put(AccountTypeEnum.DEPOSIT_ACCOUNT.getValue(), false);
              }
              return accountResponse;
            })
            .collectList()
            .flatMap(response -> Mono.just(setAccountsAvailable(accountsAvailable)));
  }

  private List<ProductAvailableDto> setAccountsAvailable(Map<String, Boolean> accountsAvailable) {

    for(AccountTypeEnum accountTypeEnum : AccountTypeEnum.values()) {
      accountsAvailable.putIfAbsent(accountTypeEnum.getValue(), true);
    }

    List<ProductAvailableDto> productAvailableDtoList = new ArrayList<>();
    accountsAvailable.forEach((key, value) -> {
      if (Boolean.TRUE.equals(value)) {
        productAvailableDtoList.add(ProductAvailableDto.builder()
                .productName("ACCOUNT")
                .productDescription("DISPONIBLE")
                .productCategory(key)
                .build());
      }
    });

    return productAvailableDtoList;
  }

  private Mono<List<ProductAvailableDto>> validateCreditProductsForCustomerType(Customer customer) {
    return (customer.getCustomerType().equals(CustomerTypeEnum.BUSINESS.getValue()))
            ? validateCreditProductsForBusinessCustomer(customer)
            : validateCreditProductsForPersonalCustomer(customer);
  }

  private Mono<List<ProductAvailableDto>> validateCreditProductsForPersonalCustomer(Customer customer) {
    return customerBusinessUtil.findCreditByCustomerId(customer.getCustomerId())
            .flatMap(creditDto -> (creditDto != null)
                    ? Mono.empty()
                    : Mono.just(setCreditProductAvailable())
            )
            .onErrorResume(throwable -> Mono.just(setCreditProductAvailable()));
  }

  private Mono<List<ProductAvailableDto>> validateCreditProductsForBusinessCustomer(Customer customer) {
    List<ProductAvailableDto> productAvailableDtoList = new ArrayList<>();
    productAvailableDtoList.add(ProductAvailableDto.builder()
                    .productName("CREDIT")
                    .productDescription("DISPONIBLE")
                    .productCategory("CREDIT BUSINESS")
                    .build());
    return Mono.just(productAvailableDtoList);
  }

  private List<ProductAvailableDto> setCreditProductAvailable() {
    List<ProductAvailableDto> productAvailableDtoList = new ArrayList<>();
    productAvailableDtoList.add(ProductAvailableDto.builder()
                    .productName("CREDIT")
                    .productDescription("DISPONIBLE")
                    .productCategory("CREDIT PERSONAL")
                    .build());
    return productAvailableDtoList;
  }
}
