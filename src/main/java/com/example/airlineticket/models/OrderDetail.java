package com.example.airlineticket.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String txnRef;
    private String name;
    private String email;
    private String phoneNumber;
    private int totalPrice;
    private String flightNumber;
    private String status;
    private Date date;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Customer> customers;
    @ManyToOne
    private User user;

}
