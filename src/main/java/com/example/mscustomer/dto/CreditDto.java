package com.example.mscustomer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Credit Dto.
 *
 * @author Alisson Arteaga / Christian Dionisio
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditDto {
  private String creditId;

  private BigDecimal creditBalance;

  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]")
  private LocalDateTime paymentDate;

  private Integer timeLimit;

  private String initialDate;

  private BigDecimal monthlyFee;

  private String creditType;

  private String customerId;

  private Integer interestRate;
}
