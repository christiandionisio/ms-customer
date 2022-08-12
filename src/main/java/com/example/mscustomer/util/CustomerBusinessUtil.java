package com.example.mscustomer.util;

import com.example.mscustomer.dto.AccountDto;
import com.example.mscustomer.dto.CreditDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerBusinessUtil {

    @Value("${account.service.uri}")
    private String uriAccountService;

    @Value("${credit.service.uri}")
    private String uriCreditService;

    public Flux<AccountDto> findByCustomerOwnerId(String id) {
        return WebClient.create(uriAccountService)
                .get()
                .uri("/findByCustomerOwnerId/{id}", id)
                .retrieve()
                .bodyToFlux(AccountDto.class);
    }

    public Mono<CreditDto> findCreditByCustomerId(String id) {
        return WebClient.create(uriCreditService)
                .get()
                .uri("/findByCustomerId/{id}", id)
                .retrieve()
                .bodyToMono(CreditDto.class);
    }

}
