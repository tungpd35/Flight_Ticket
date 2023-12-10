package com.example.airlineticket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto {
    private String status;
    private String message;
    private String url;
}
