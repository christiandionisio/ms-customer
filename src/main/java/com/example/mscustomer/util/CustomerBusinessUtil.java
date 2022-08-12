package com.example.mscustomer.util;

import com.example.mscustomer.dto.AccountDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class CustomerBusinessUtil {

    @Value("${account.service.uri}")
    private String uriAccountService;

    public Flux<AccountDto> findByCustomerOwnerId(String id) {
        return WebClient.create(uriAccountService)
                .get()
                .uri("/findByCustomerOwnerId/{id}", id)
                .retrieve()
                .bodyToFlux(AccountDto.class);
    }

}
