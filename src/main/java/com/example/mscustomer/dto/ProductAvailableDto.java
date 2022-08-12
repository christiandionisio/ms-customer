package com.example.mscustomer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductAvailableDto {
    private String productName;
    private String productCategory;
    private String productDescription;
}
