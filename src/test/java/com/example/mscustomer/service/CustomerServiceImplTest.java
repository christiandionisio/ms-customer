package com.example.mscustomer.service;

import com.example.mscustomer.dto.AccountDto;
import com.example.mscustomer.enums.AccountTypeEnum;
import com.example.mscustomer.enums.CustomerTypeEnum;
import com.example.mscustomer.error.PersonalCustomerNotHaveAnAccountException;
import com.example.mscustomer.model.Customer;
import com.example.mscustomer.repository.CustomerRepository;
import com.example.mscustomer.util.CustomerBusinessUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@SpringBootTest
class CustomerServiceImplTest {

    Logger logger = Logger.getLogger(CustomerServiceImplTest.class.getName());

    @MockBean
    private CustomerRepository customerDao;

    @MockBean
    private CustomerBusinessUtil customerBusinessUtil;

    @Autowired
    private CustomerServiceImpl customerService;

    @Test
    void getSummaryOfAvailableProductsByPersonalCustomerTest() {
        Mockito.when(customerDao.findById(Mockito.anyString()))
                .thenReturn(Mono.just(getPersonalCustomerTest()));

        Mockito.when(customerBusinessUtil.findByCustomerOwnerId(Mockito.anyString()))
                .thenReturn(Flux.empty());

        Mockito.when(customerBusinessUtil.findCreditByCustomerId(Mockito.anyString()))
                .thenReturn(Mono.error(new PersonalCustomerNotHaveAnAccountException()));

        StepVerifier.create(customerService.getSummaryOfAvailableProductsByCustomer("1"))
                .consumeNextWith(availableProductsDto -> {
                    logger.info("AvailableProductsDto: " + availableProductsDto.toString());
                    Assertions.assertEquals(3, availableProductsDto.getAccountProductDtoList().size());
                    Assertions.assertEquals(1, availableProductsDto.getCreditProductDtoList().size());
                    Assertions.assertEquals(1, availableProductsDto.getCardProductDtoList().size());
                })
                .verifyComplete();
    }

    @Test
    void getSummaryOfAvailableProductsByPersonalCustomerWithoutAccountAvailabilityTest() {
        Mockito.when(customerDao.findById(Mockito.anyString()))
                .thenReturn(Mono.just(getPersonalCustomerTest()));

        Mockito.when(customerBusinessUtil.findByCustomerOwnerId(Mockito.anyString()))
                .thenReturn(getAccountDtoListTest());

        Mockito.when(customerBusinessUtil.findCreditByCustomerId(Mockito.anyString()))
                .thenReturn(Mono.error(new PersonalCustomerNotHaveAnAccountException()));

        StepVerifier.create(customerService.getSummaryOfAvailableProductsByCustomer("1"))
                .consumeNextWith(availableProductsDto -> {
                    logger.info("AvailableProductsDto: " + availableProductsDto.toString());
                    Assertions.assertEquals(0, availableProductsDto.getAccountProductDtoList().size());
                    Assertions.assertEquals(1, availableProductsDto.getCreditProductDtoList().size());
                    Assertions.assertEquals(1, availableProductsDto.getCardProductDtoList().size());
                })
                .verifyComplete();
    }

    @Test
    void getSummaryOfAvailableProductsByBusinessCustomerTest() {
        Mockito.when(customerDao.findById(Mockito.anyString()))
                .thenReturn(Mono.just(getBusinessCustomerTest()));

        Mockito.when(customerBusinessUtil.findByCustomerOwnerId(Mockito.anyString()))
                .thenReturn(Flux.empty());

        Mockito.when(customerBusinessUtil.findCreditByCustomerId(Mockito.anyString()))
                .thenReturn(Mono.error(new PersonalCustomerNotHaveAnAccountException()));

        StepVerifier.create(customerService.getSummaryOfAvailableProductsByCustomer("1"))
                .consumeNextWith(availableProductsDto -> {
                    logger.info("AvailableProductsDto: " + availableProductsDto.toString());
                    Assertions.assertEquals(1, availableProductsDto.getAccountProductDtoList().size());
                    Assertions.assertEquals(1, availableProductsDto.getCreditProductDtoList().size());
                    Assertions.assertEquals(1, availableProductsDto.getCardProductDtoList().size());
                })
                .verifyComplete();
    }

    private Customer getPersonalCustomerTest() {
        Customer customer = new Customer();
        customer.setCustomerId("1");
        customer.setName("John Doe");
        customer.setCustomerType(CustomerTypeEnum.PERSONNEL.getValue());
        customer.setCategory("Personal");
        return customer;
    }

    private Customer getBusinessCustomerTest() {
        Customer customer = new Customer();
        customer.setCustomerId("1");
        customer.setName("John Doe");
        customer.setCustomerType(CustomerTypeEnum.BUSINESS.getValue());
        customer.setCategory("Business");
        return customer;
    }

    private Flux<AccountDto> getAccountDtoListTest() {
        AccountDto accountDto1 = new AccountDto();
        accountDto1.setAccountId("1");
        accountDto1.setAccountType(AccountTypeEnum.CURRENT_ACCOUNT.getValue());

        AccountDto accountDto2 = new AccountDto();
        accountDto2.setAccountId("1");
        accountDto2.setAccountType(AccountTypeEnum.SAVING_ACCOUNT.getValue());

        AccountDto accountDto3 = new AccountDto();
        accountDto3.setAccountId("1");
        accountDto3.setAccountType(AccountTypeEnum.DEPOSIT_ACCOUNT.getValue());

        List<AccountDto> accountDtoList = new ArrayList<>();
        accountDtoList.add(accountDto1);
        accountDtoList.add(accountDto2);
        accountDtoList.add(accountDto3);

        return Flux.fromIterable(accountDtoList);
    }


}