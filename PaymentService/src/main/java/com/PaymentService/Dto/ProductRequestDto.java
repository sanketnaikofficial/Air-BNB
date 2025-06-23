package com.PaymentService.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
    private Double amount;
    private Long quantity;
    private String name;
    private String currency;
    private Long bookingId;
}
