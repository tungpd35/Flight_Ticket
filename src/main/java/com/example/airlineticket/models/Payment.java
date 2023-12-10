package com.example.airlineticket.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Payment {
    private String url;
    private String txnRef;

    public Payment(String url, String vnp_TxnRef) {
        this.url = url;
        this.txnRef = vnp_TxnRef;
    }
}
