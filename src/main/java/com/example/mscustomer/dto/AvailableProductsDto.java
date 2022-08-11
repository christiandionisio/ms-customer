package com.example.mscustomer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableProductsDto {

    private List<ProductAvailableDto> accountProductDtoList;
    private List<ProductAvailableDto> cardProductDtoList;
    private List<ProductAvailableDto> creditProductDtoList;

}
