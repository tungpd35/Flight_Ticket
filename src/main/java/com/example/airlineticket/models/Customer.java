package com.example.airlineticket.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Age age;
    private Date birth;
    private String sex;
    private String address;
    private String phoneNumber;
    @OneToOne
    private Baggage baggage;
    @OneToOne(cascade = CascadeType.ALL)
    private Ticket ticket;
    @ManyToOne
    private Agency agency;
}
